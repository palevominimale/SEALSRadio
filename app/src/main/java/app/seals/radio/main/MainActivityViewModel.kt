package app.seals.radio.main

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.seals.radio.data.preferences.SharedPrefsManager
import app.seals.radio.domain.usecases.api_ops.GetListBySearchUseCase
import app.seals.radio.domain.usecases.api_ops.GetListWithFilterUseCase
import app.seals.radio.domain.usecases.api_ops.GetTopListUseCase
import app.seals.radio.domain.usecases.local_storage.CurrentListUseCase
import app.seals.radio.domain.usecases.local_storage.FavoriteListUseCase
import app.seals.radio.entities.api.ApiResult
import app.seals.radio.entities.responses.StationModel
import app.seals.radio.intents.MainIntent
import app.seals.radio.player.BackgroundPlayerService
import app.seals.radio.states.PlayerState
import app.seals.radio.states.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainActivityViewModel(
    private val getTop: GetTopListUseCase,
    private val getByFilter: GetListWithFilterUseCase,
    private val getBySearch: GetListBySearchUseCase,
    private val prefs: SharedPrefsManager,
    private val current: CurrentListUseCase,
    private val favorite: FavoriteListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Splash)
    private val _pState = MutableStateFlow<PlayerState>(PlayerState.IsStopped(StationModel()))
    private val _apiState = MutableStateFlow<ApiResult>(ApiResult.ApiSuccess(emptyList()))
    private val _currentStation = mutableStateOf(StationModel())
    val uiState get() = _state as StateFlow<UiState>
    val playerState get() = _pState as StateFlow<PlayerState>
    private val scope = CoroutineScope(Dispatchers.IO)
    @SuppressLint("StaticFieldLeak")
    private var playerService : BackgroundPlayerService? = null

    init {
        viewModelScope.launch {
            _apiState.collectLatest {
                when(it) {
                    is ApiResult.ApiError -> _state.emit(UiState.Error(it.code, it.message))
                    is ApiResult.ApiException -> _state.emit(UiState.Exception(it.e))
                    is ApiResult.ApiSuccess -> {
                        if(it.data.isNotEmpty()) when(it.data[0]) {
                            is StationModel -> {
                                current.clear()
                                current.set(it.data as List<StationModel>)
                                if(_currentStation.value.url == null) {
                                    if(_pState.value is PlayerState.IsStopped) {
                                        _pState.emit(PlayerState.IsStopped(it.data[0] as StationModel))
                                    } else {
                                        _pState.emit(PlayerState.IsPlaying(it.data[0] as StationModel))
                                    }
                                    _currentStation.value = it.data[0] as StationModel
                                }
                                _state.emit(UiState.Ready.Main(
                                    list = it.data as List<StationModel>,
                                    favs = favorite.getList(),
                                    filterIsShown = false,
                                    filterOptions = prefs.getFilter()
                                ))
                            }
                            else -> _state.emit(UiState.Ready.Empty)
                        }
                        else _state.emit(UiState.Ready.Empty)
                    }
                }
            }
        }
    }

    fun intent(intent: MainIntent) {
        viewModelScope.launch {
            when(intent) {
                is MainIntent.Play -> {
                    _pState.emit(PlayerState.IsPlaying(_currentStation.value))
                    playerService?.play()
                }
                is MainIntent.Stop -> {
                    _pState.emit(PlayerState.IsStopped(_currentStation.value))
                    playerService?.pause()
                }
                is MainIntent.Next -> {}
                is MainIntent.Previous -> {}
                is MainIntent.Search -> {
                    prefs.setLastSearch(intent.options)
                    scope.launch {
                        _apiState.emit(getBySearch.execute())
                    }
                }
                is MainIntent.SetFilter -> {
                    prefs.setFilter(intent.options)
                    scope.launch {
                        if(
                            intent.options.country == null
                            && intent.options.tags == null
                            && intent.options.language == null
                        ) {
                            _apiState.emit(getTop.execute())
                        } else {
                            _apiState.emit(getByFilter.execute())
                        }
                    }
                }
                is MainIntent.SwitchFilter -> {
                    if(_state.value is UiState.Ready.Main) {
                        if((_state.value as UiState.Ready.Main).filterIsShown) {
                            _state.emit(UiState.Ready.Main(
                                list = current.get(),
                                favs = favorite.getList(),
                                filterOptions = prefs.getFilter(),
                                filterIsShown = false
                            ))
                        } else {
                            _state.emit(UiState.Ready.Main(
                                list = current.get(),
                                filterOptions = prefs.getFilter(),
                                filterIsShown = true
                            ))
                        }
                    }
                }
                is MainIntent.ShowMain -> _state.emit(UiState.Ready.Main(
                    list = current.get(),
                    filterOptions = prefs.getFilter(),
                    filterIsShown = false
                ))
                is MainIntent.AddFavorite -> {
                    favorite.add(intent.options)
                    _state.emit(UiState.Ready.Main(
                        list = current.get(),
                        favs = favorite.getList(),
                        filterOptions = prefs.getFilter(),
                        filterIsShown = false
                    ))
                }
                is MainIntent.DelFavorite -> {
                    favorite.delete(intent.options)
                    _state.emit(UiState.Ready.Main(
                        list = current.get(),
                        favs = favorite.getList(),
                        filterOptions = prefs.getFilter(),
                        filterIsShown = false
                    ))
                }
                is MainIntent.Select -> selectStation(intent.options)
                is MainIntent.ShowFavorites -> {}
            }
        }
    }

    fun getCurrentSavedList() {
        viewModelScope.launch {
            _apiState.emit(ApiResult.ApiSuccess(current.get()))
        }
    }

    private fun selectStation(station: StationModel) {
        _currentStation.value = station
        playerService?.updatePlayer(station)
        viewModelScope.launch {
            when(_pState.value) {
                is PlayerState.IsStopped -> {
                    _pState.emit(PlayerState.IsStopped(_currentStation.value))
                }
                is PlayerState.IsPlaying -> {
                    _pState.emit(PlayerState.IsPlaying(_currentStation.value))
                    playerService?.play()
                }
            }
        }
    }

    fun setPlayer(playerService: BackgroundPlayerService) {
        this.playerService = playerService
        playerService.updatePlayer(_currentStation.value)
        viewModelScope.launch {
            playerService.isPlayingStateFlow.collect {
                if(it) _pState.emit(PlayerState.IsPlaying(_currentStation.value))
                else _pState.emit(PlayerState.IsStopped(_currentStation.value))
            }
        }
    }

    fun unsetPlayer() {
        playerService?.stopSelf()
        this.playerService = null
    }
}
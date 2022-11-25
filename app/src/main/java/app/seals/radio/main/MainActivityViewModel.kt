package app.seals.radio.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.seals.radio.data.preferences.SharedPrefsManager
import app.seals.radio.domain.interfaces.LocalRepo
import app.seals.radio.domain.models.FilterOptions
import app.seals.radio.domain.usecases.api_ops.GetListBySearchUseCase
import app.seals.radio.domain.usecases.api_ops.GetListWithFilterUseCase
import app.seals.radio.domain.usecases.api_ops.GetTopListUseCase
import app.seals.radio.domain.usecases.local_storage.CurrentListUseCase
import app.seals.radio.domain.usecases.local_storage.FavoriteListUseCase
import app.seals.radio.states.MainUiState
import app.seals.radio.entities.api.ApiResult
import app.seals.radio.entities.responses.StationModel
import app.seals.radio.intents.PlayerIntent
import app.seals.radio.states.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MAVM_"

@Suppress("UNCHECKED_CAST")
class MainActivityViewModel(
    private val getTop: GetTopListUseCase,
    private val getByFilter: GetListWithFilterUseCase,
    private val getBySearch: GetListBySearchUseCase,
    private val prefs: SharedPrefsManager,
    private val current: CurrentListUseCase,
    private val favorite: FavoriteListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MainUiState>(MainUiState.Splash)
    private val _pState = MutableStateFlow<PlayerState>(PlayerState.IsStopped(StationModel()))
    private val _fState = MutableStateFlow(false)
    private val _apiState = MutableStateFlow<ApiResult?>(null)
    private val _currentStation = mutableStateOf(StationModel())
    val uiState get() = _state as StateFlow<MainUiState>
    val filterState get() = _fState as StateFlow<Boolean>
    val playerState get() = _pState as StateFlow<PlayerState>
    private var filterOptions = FilterOptions()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        filterOptions = prefs.getFilter()
        viewModelScope.launch {
            _apiState.collectLatest {
                when(it) {
                    is ApiResult.ApiError -> {
                        _state.emit(MainUiState.Error(it.code, it.message))
                    }
                    is ApiResult.ApiException -> {
                        _state.emit(MainUiState.Exception(it.e))
                    }
                    is ApiResult.ApiSuccess -> {
                        if(it.data.isNotEmpty()) {
                            when(it.data[0]) {
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
                                    _state.emit(MainUiState.StationListReady(it.data as List<StationModel>))
                                }
                                else -> {
                                    _state.emit(MainUiState.IsLoading)
                                }
                            }
                        } else {
                            _state.emit(MainUiState.Error(404, "Nothing has been found"))
                        }
                    }
                    else -> _state.emit(MainUiState.Splash)
                }
            }
        }
    }

    fun playerIntent(intent: PlayerIntent, backgroundPlayerServiceState: Boolean) {
        viewModelScope.launch {
            when(intent) {
                is PlayerIntent.Play -> {
                    if(!backgroundPlayerServiceState) play()
                }
                is PlayerIntent.Stop -> {
                    if(backgroundPlayerServiceState) pause()
                }
                is PlayerIntent.Next -> {}
                is PlayerIntent.Previous -> {}
            }
        }
    }

    fun getTopList() {
        scope.launch {
            _apiState.emit(getTop.execute())
        }
    }

    fun getCurrentSavedList() {
        viewModelScope.launch {
            _apiState.emit(ApiResult.ApiSuccess(current.get()))
        }
    }

    fun selectStation(station: StationModel) {
        _currentStation.value = station
        viewModelScope.launch {
            when(_pState.value) {
                is PlayerState.IsStopped -> {
                    _pState.emit(PlayerState.IsStopped(_currentStation.value))
                }
                is PlayerState.IsPlaying -> {
                    _pState.emit(PlayerState.IsPlaying(_currentStation.value))
                }
            }
        }
    }

    fun play() {
        viewModelScope.launch {
            _pState.emit(PlayerState.IsPlaying(_currentStation.value))
        }
    }

    fun pause() {
        viewModelScope.launch {
            _pState.emit(PlayerState.IsStopped(_currentStation.value))
        }
    }

    fun search() {
        scope.launch {
            _apiState.emit(getBySearch.execute())
        }
    }

    fun setFilter(options: FilterOptions) {
        filterOptions = options
        prefs.setFilter(options)
        getByFilter()
    }

    fun getFilter() : FilterOptions {
        filterOptions = prefs.getFilter()
        return filterOptions
    }

    fun setLastSearch(search: String) {
        prefs.setLastSearch(search)
    }

    fun showFilter() {
        viewModelScope.launch {
            _fState.emit(true)
        }
    }

    fun hideFilter() {
        viewModelScope.launch {
            _fState.emit(false)
        }
    }

    fun getFavorites() : List<StationModel> {
        return favorite.getList()
    }

    fun getCurrentList() : List<StationModel> {
        return current.get()
    }

    fun getFavoritesUuids() : List<String> {
        val favorites = favorite.getList()
        val list = mutableListOf<String>().apply {
            favorites.forEach {
                if (it.stationuuid != null) {
                    add(it.stationuuid!!)
                }
            }
        }.toList()
        return list
    }

    fun addFavorite(station: StationModel) {
        favorite.add(station)
    }

    fun delFavorite(uuid: String) {
        favorite.delete(uuid)
    }

    private fun getByFilter() {
        scope.launch {
            _apiState.emit(getByFilter.execute())
        }
    }
}
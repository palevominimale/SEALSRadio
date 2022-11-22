package app.seals.radio.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import app.seals.radio.domain.usecases.GetTopListUseCase
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

@Suppress("UNCHECKED_CAST")
class MainActivityViewModel(
    private val getTop: GetTopListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MainUiState>(MainUiState.Splash)
    private val _pState = MutableStateFlow<PlayerState>(PlayerState.IsStopped(StationModel()))
    private val _apiState = MutableStateFlow<ApiResult?>(null)
    private val _currentStation = mutableStateOf(StationModel())
    val uiState get() = _state as StateFlow<MainUiState>
    val playerState get() = _pState as StateFlow<PlayerState>

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        viewModelScope.launch {
            _apiState.collectLatest {
                when(it) {
                    is ApiResult.ApiError -> {
                        Log.e("MAVM_api_err", "$it")
                        _state.emit(MainUiState.Error(it.code, it.message))
                    }
                    is ApiResult.ApiException -> {
                        Log.e("MAVM_api_exc", "$it")
                        _state.emit(MainUiState.Exception(it.e))
                    }
                    is ApiResult.ApiSuccess -> {
                        Log.e("MAVM_api_scs", "$it")
                        when(it.data[0]) {
                            is StationModel -> {
                                if(_currentStation.value.url == null) {
                                    if(_pState.value is PlayerState.IsStopped) {
                                        _pState.emit(PlayerState.IsStopped(it.data[0] as StationModel))
                                    } else {
                                        _pState.emit(PlayerState.IsPlaying(it.data[0] as StationModel))
                                    }
                                    _currentStation.value = it.data[0] as StationModel
                                }
                                _state.emit(MainUiState.StationListReady(it.data as List<StationModel>))
                                Log.e("MAVM_state", "${it.data}")
                            }
                            else -> {
                                _state.emit(MainUiState.IsLoading)
                                Log.e("MAVM_state_else", "${it.data}")
                            }
                        }
                    }
                    else -> _state.emit(MainUiState.Splash)
                }
            }
        }
    }

    fun playerIntent(intent: PlayerIntent, backgroundPlayerServiceState: Boolean) {
        viewModelScope.launch {
            Log.e("MAVM_player_intent", "$intent")
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

    fun getTop() {
        scope.launch {
            _apiState.emit(getTop.execute())
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
        Log.e("MAVM_", "${station.name} ${station.urlResolved} ")
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

    private fun next() {

    }
    private fun prev() {

    }

}
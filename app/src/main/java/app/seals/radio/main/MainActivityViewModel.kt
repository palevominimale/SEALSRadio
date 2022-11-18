package app.seals.radio.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.seals.radio.domain.usecases.GetTopListUseCase
import app.seals.radio.states.MainUiState
import app.seals.radio.entities.api.ApiResult
import app.seals.radio.entities.responses.StationModel
import app.seals.radio.intents.PlayerIntent
import app.seals.radio.player.PlayerService
import app.seals.radio.states.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainActivityViewModel(
    private val getTop: GetTopListUseCase,
    private val player: PlayerService
) : ViewModel() {

    private val _state = MutableStateFlow<MainUiState>(MainUiState.Splash)
    private val _pState = MutableStateFlow<PlayerState>(PlayerState.IsStopped(StationModel()))
    private val _apiState = MutableStateFlow<ApiResult?>(null)
    private val _currentStation = mutableStateOf(StationModel())
    val uiState get() = _state
    val playerState get() = _pState

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        viewModelScope.launch {
            _apiState.collectLatest {
                when(it) {
                    is ApiResult.ApiError -> {
                        Log.e("MAVM_api_err", "$it")
                        uiState.emit(MainUiState.Error(it.code, it.message))
                    }
                    is ApiResult.ApiException -> {
                        Log.e("MAVM_api_exc", "$it")
                        uiState.emit(MainUiState.Exception(it.e))
                    }
                    is ApiResult.ApiSuccess -> {
                        Log.e("MAVM_api_scs", "$it")
                        when(it.data[0]) {
                            is StationModel -> {
                                uiState.emit(MainUiState.StationListReady(it.data as List<StationModel>))
                                Log.e("MAVM_state", "${it.data}")
                            }
                            else -> {
                                uiState.emit(MainUiState.IsLoading)
                                Log.e("MAVM_state_else", "${it.data}")
                            }
                        }
                    }
                    else -> uiState.emit(MainUiState.Splash)
                }
            }
        }
    }

    fun playerIntent(intent: PlayerIntent) {
        viewModelScope.launch {
            Log.e("MAVM_player_intent", "$intent")
            when(intent) {
                is PlayerIntent.Play -> play()
                is PlayerIntent.Stop -> stop()
                is PlayerIntent.Next -> next()
                is PlayerIntent.Previous -> prev()
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
        player.setUrl(station.urlResolved ?: "")
        viewModelScope.launch {
            when(_pState.value) {
                is PlayerState.IsStopped -> {
                    _pState.emit(PlayerState.IsStopped(_currentStation.value))
                    player.stop()
                }
                is PlayerState.IsPlaying -> {
                    _pState.emit(PlayerState.IsPlaying(_currentStation.value))
                    player.play()
                }
            }
        }
        Log.e("MAVM_", "${station.name} ${station.urlResolved} ")
    }

    private fun play() {
        viewModelScope.launch {
            _pState.emit(PlayerState.IsPlaying(_currentStation.value))
        }
        player.play()
    }
    private fun stop() {
        viewModelScope.launch {
            _pState.emit(PlayerState.IsStopped(_currentStation.value))
        }
        player.stop()
    }
    private fun next() {

    }
    private fun prev() {

    }

}
package app.seals.radio.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.seals.radio.domain.usecases.GetTopListUseCase
import app.seals.radio.states.MainUiState
import app.seals.radio.entities.api.ApiResult
import app.seals.radio.entities.responses.StationModel
import app.seals.radio.states.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getTop: GetTopListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MainUiState>(MainUiState.IsLoading)
    private val _pState = MutableStateFlow<PlayerState>(PlayerState.IsStopped(StationModel()))
    private val _apiState = MutableStateFlow<ApiResult>(ApiResult.ApiError(666, "not loaded"))
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
                }
            }
        }
    }

    fun getTop() {
        scope.launch {
            _apiState.emit(getTop.execute())
        }
    }

    fun selectStation(uuid: String) {

    }

}
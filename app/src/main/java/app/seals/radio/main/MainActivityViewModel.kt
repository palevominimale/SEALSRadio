package app.seals.radio.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.seals.radio.domain.usecases.GetTopListUseCase
import app.seals.radio.ui.UiState
import app.seals.radio.entities.api.ApiResult
import app.seals.radio.entities.responses.StationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getTop: GetTopListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.IsLoading)
    private val _apiState = MutableStateFlow<ApiResult>(ApiResult.ApiError(666, "not loaded"))
    val state get() = _state
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        viewModelScope.launch {
            _apiState.collectLatest {
                when(it) {
                    is ApiResult.ApiError -> {
                        Log.e("MAVM_api_err", "$it")
                        state.emit(UiState.Error(it.code, it.message))
                    }
                    is ApiResult.ApiException -> {
                        Log.e("MAVM_api_exc", "$it")
                        state.emit(UiState.Exception(it.e))
                    }
                    is ApiResult.ApiSuccess -> {
                        Log.e("MAVM_api_scs", "$it")
                        when(it.data[0]) {
                            is StationModel -> {
                                state.emit(UiState.StationListReady(it.data as List<StationModel>))
                                Log.e("MAVM_state", "${it.data}")
                            }
                            else -> {
                                state.emit(UiState.IsLoading)
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
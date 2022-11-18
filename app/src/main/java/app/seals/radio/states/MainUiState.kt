package app.seals.radio.states

import app.seals.radio.entities.responses.StationModel

sealed interface MainUiState {
    object IsLoading : MainUiState
    data class Error (val code: Int? = null, val message: String? = null) : MainUiState
    data class Exception (val e: Throwable? = null) : MainUiState
    data class StationListReady (val list: List<StationModel>? = null) : MainUiState
}
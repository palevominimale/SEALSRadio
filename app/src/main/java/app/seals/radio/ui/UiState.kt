package app.seals.radio.ui

import app.seals.radio.entities.responses.StationModel

sealed interface UiState {
    object IsLoading : UiState
    data class Error (val code: Int? = null, val message: String? = null) : UiState
    data class Exception (val e: Throwable? = null) : UiState
    data class StationListReady (val list: List<StationModel>? = null) : UiState
}
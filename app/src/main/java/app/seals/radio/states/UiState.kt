package app.seals.radio.states

import app.seals.radio.domain.models.FilterOptions
import app.seals.radio.entities.responses.StationModel

sealed interface UiState {
    object Splash : UiState
    object IsLoading : UiState

    data class Error (
        val code: Int? = null,
        val message: String? = null
    ) : UiState

    data class Exception (
        val e: Throwable? = null
    ) : UiState

    sealed interface Ready : UiState {
        data class Main(
            val list: List<StationModel> = emptyList(),
            val favs: List<StationModel> = emptyList(),
            val filterIsShown: Boolean = false,
            val filterOptions: FilterOptions? = null
        ) : Ready

        data class Favorites(
            val list: List<StationModel> = emptyList()
        ) : Ready

        object Empty : Ready
    }
}
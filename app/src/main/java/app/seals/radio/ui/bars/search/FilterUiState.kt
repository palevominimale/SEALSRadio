package app.seals.radio.ui.bars.search

import app.seals.radio.domain.models.FilterOptions

sealed interface FilterUiState {
    object Hidden : FilterUiState
    object Shown : FilterUiState
}
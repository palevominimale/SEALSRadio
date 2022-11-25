package app.seals.radio.intents

import app.seals.radio.domain.models.FilterOptions

sealed interface MainIntent {
    object ShowFilter : MainIntent
    object HideFilter : MainIntent
    object ClearFilter : MainIntent
    object ShowMain : MainIntent
    object ShowFavorites : MainIntent
    object Stop : MainIntent
    object Play : MainIntent
    object Next : MainIntent
    object Previous : MainIntent
    data class Search(val options: String) : MainIntent
    data class SetFilter(val options: FilterOptions) : MainIntent
}
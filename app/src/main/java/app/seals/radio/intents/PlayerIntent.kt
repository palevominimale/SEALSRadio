package app.seals.radio.intents

sealed interface PlayerIntent {
    object Stop : PlayerIntent
    object Play : PlayerIntent
    object Next : PlayerIntent
    object Previous : PlayerIntent
}
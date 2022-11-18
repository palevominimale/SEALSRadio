package app.seals.radio.states

import app.seals.radio.entities.responses.StationModel

sealed class PlayerState(val station: StationModel) {
    data class IsPlaying(val st: StationModel) : PlayerState(st)
    data class IsStopped(val st: StationModel) : PlayerState(st)
}
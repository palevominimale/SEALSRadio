package app.seals.radio.domain.usecases

import app.seals.radio.domain.interfaces.PlayerControls

class PlayerControlsUseCase(
    private val controls: PlayerControls
) {

    fun play() {
        controls.play()
    }

    fun stop() {
        controls.stop()
    }

    fun setUrl(url: String) {
        controls.setUrl(url)
    }

}
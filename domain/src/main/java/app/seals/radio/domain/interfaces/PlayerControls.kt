package app.seals.radio.domain.interfaces

interface PlayerControls {
    fun play()
    fun stop()
    fun setUrl(url: String)
}
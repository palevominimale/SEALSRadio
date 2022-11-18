package app.seals.radio.player

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


class PlayerService(
    context: Context
) {

    private val exo = ExoPlayer.Builder(context).build()

    fun setUrl(url: String) {
        exo.stop()
        val mi = MediaItem.fromUri(url)
        exo.setMediaItem(mi)
        exo.prepare()
        exo.playWhenReady = false
    }

    fun play() {
        exo.play()
    }

    fun stop() {
        exo.pause()
    }
}
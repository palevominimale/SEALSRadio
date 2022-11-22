package app.seals.radio.player

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.view.ContentInfoCompat
import app.seals.radio.entities.responses.StationModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.FlagSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BackgroundPlayerService : Service() {

    private val backgroundPlayIBinder: IBinder = BackgroundServiceBinder()

    private var exoPlayer: ExoPlayer? = null
    private var station: StationModel = StationModel()// object that can hold details for the playing item, title/url/etc
    private val channelId = "Exoplayer Channel"
    private val notificationId: Int = 123987
    private var playbackPos: Long = 0
    private var playWhenReady = true
    private val _playerStateFlow = MutableStateFlow(false)
    private lateinit var context: Context
    private lateinit var playerNotificationManager: PlayerNotificationManager
    val isPlayingStateFlow get() = _playerStateFlow.asStateFlow()

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            MainScope().launch {
                Log.d("BPS_", "playing: $isPlaying")
                _playerStateFlow.emit(isPlaying)
            }
        }
    }

    private fun initializePlayer(context: Context){
        if (exoPlayer == null)
            exoPlayer = ExoPlayer.Builder(context).build()
        val mediaItem = MediaItem.fromUri(station.url?: "")
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            playWhenReady = true
            seekTo(playbackPos)
            prepare()
            addListener(playerListener)
        }
        initPlayerNotificationManager()
        playerNotificationManager.setPlayer(exoPlayer)
    }

    private fun initPlayerNotificationManager() {
        playerNotificationManager = PlayerNotificationManager.Builder(
            context,
            notificationId,
            channelId
        ).apply {
            setMediaDescriptionAdapter(
                object : PlayerNotificationManager.MediaDescriptionAdapter{
                override fun getCurrentContentTitle(player: Player): CharSequence {
                    return station.name ?: "unknown"
                }
                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    val intent = Intent()
                    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }
                override fun getCurrentContentText(player: Player): CharSequence {
                    return station.tags ?: "no tags"
                }
                override fun getCurrentSubText(player: Player): CharSequence {
                    return "a subtitle"
                }
                override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
                    return null
                }
            })
            setNotificationListener(
                object : PlayerNotificationManager.NotificationListener{
                    override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
                        startForeground(notificationId, notification)
                    }
                    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                        stopSelf()
                        stopForeground(true)
                    }
                }
            )
            setSmallIconResourceId(R.drawable.ic_radio)
            setStopActionIconResourceId(R.drawable.ic_stop)
            setPlayActionIconResourceId(R.drawable.ic_play)
            setNextActionIconResourceId(R.drawable.ic_skip_next)
            setPreviousActionIconResourceId(R.drawable.ic_skip_previous)
            setChannelDescriptionResourceId(R.string.player_channel_description)
            setChannelNameResourceId(R.string.player_channel_name)
        }.build()
        playerNotificationManager.apply {
            setColor(Color.LTGRAY)
            setBadgeIconType(Notification.BADGE_ICON_NONE)
            setPlayer(exoPlayer)
            setUseNextAction(true)
            setUsePlayPauseActions(true)
//            setUseStopAction(true)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }
    }

    fun play() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun getPlayingMediaID() = station.stationuuid

    private fun releasePlayer(){
        playerNotificationManager.setPlayer(null)
        if (exoPlayer != null){
            playWhenReady = exoPlayer!!.playWhenReady
            playbackPos = exoPlayer!!.currentPosition
            exoPlayer?.release()
            exoPlayer = null
        }
    }

    fun updatePlayer(station: StationModel){
        this.station = station
        val mediaItem = MediaItem.fromUri(this.station.url ?: "")
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = false
    }

    override fun onBind(intent: Intent?): IBinder {
        initializePlayer(context)
        return backgroundPlayIBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        stopSelf()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        stopSelf()
    }

    inner class BackgroundServiceBinder: Binder() {
        fun getService() =this@BackgroundPlayerService
        fun getPlayingMediaID() = station.stationuuid
    }

}
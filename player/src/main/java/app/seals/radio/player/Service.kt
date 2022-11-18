package app.seals.radio.player

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class PService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

    }
}
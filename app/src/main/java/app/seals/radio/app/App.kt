package app.seals.radio.app

import android.app.Application
import app.seals.radio.di.dataDi
import app.seals.radio.di.domainDi
import app.seals.radio.di.playerDi
import app.seals.radio.di.uiDi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    uiDi,
                    domainDi,
                    dataDi,
                    playerDi
                )
            )
        }
    }
}
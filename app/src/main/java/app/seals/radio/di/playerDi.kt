package app.seals.radio.di

import app.seals.radio.player.PlayerService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val playerDi = module{
    single {
        PlayerService(
            context = androidContext()
        )
    }
}
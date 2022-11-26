package app.seals.radio.di

import app.seals.radio.data.api.NetworkRequest
import app.seals.radio.data.preferences.SharedPrefsManager
import app.seals.radio.data.repos.LocalRepoImpl
import app.seals.radio.data.repos.RemoteRepoImpl
import app.seals.radio.domain.interfaces.LocalRepo
import app.seals.radio.domain.interfaces.PrefsRepo
import app.seals.radio.domain.interfaces.RemoteRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataDi = module {
    single <RemoteRepo> {
        RemoteRepoImpl(
            request = get()
        )
    }

    single <LocalRepo> {
        LocalRepoImpl(
            context = androidContext()
        )
    }

    single <PrefsRepo> {
        SharedPrefsManager(
            context = androidContext()
        )
    }

    single {
        SharedPrefsManager(
            context = androidContext()
        )
    }

    single {
        NetworkRequest(
            prefs = get()
        )
    }

}
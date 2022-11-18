package app.seals.radio.di

import app.seals.radio.data.api.NetworkRequest
import app.seals.radio.data.repos.RemoteRepoImpl
import app.seals.radio.domain.interfaces.RemoteRepo
import org.koin.dsl.module

val dataDi = module {
    single <RemoteRepo> {
        RemoteRepoImpl(
            request = NetworkRequest()
        )
    }
}
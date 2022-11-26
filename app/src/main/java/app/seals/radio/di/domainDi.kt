package app.seals.radio.di

import app.seals.radio.domain.usecases.api_ops.GetListBySearchUseCase
import app.seals.radio.domain.usecases.api_ops.GetListWithFilterUseCase
import app.seals.radio.domain.usecases.api_ops.GetTopListUseCase
import app.seals.radio.domain.usecases.local_storage.CurrentListUseCase
import app.seals.radio.domain.usecases.local_storage.FavoriteListUseCase
import app.seals.radio.domain.usecases.prefs.PreferencesInteractionsUseCase
import org.koin.dsl.module

val domainDi = module {
    single {
        GetTopListUseCase(
            repo = get()
        )
    }
    single {
        GetListWithFilterUseCase(
            repo = get()
        )
    }

    single {
        GetListBySearchUseCase(
            repo = get()
        )
    }

    single {
        FavoriteListUseCase(
            repo = get()
        )
    }

    single {
        CurrentListUseCase(
            repo = get()
        )
    }

    single {
        PreferencesInteractionsUseCase(
            prefs = get()
        )
    }
}
package app.seals.radio.di

import app.seals.radio.domain.usecases.GetListWithFilterUseCase
import app.seals.radio.domain.usecases.GetTopListUseCase
import app.seals.radio.domain.usecases.SetFilterUseCase
import app.seals.radio.domain.usecases.UnsetFilterUseCase
import org.koin.dsl.module

val domainDi = module {
    factory {
        GetTopListUseCase(
            repo = get()
        )
    }
    factory {
        GetListWithFilterUseCase(
            repo = get()
        )
    }

    factory {
        SetFilterUseCase(
            filterControls = get()
        )
    }

    factory {
        UnsetFilterUseCase(
            filterControls = get()
        )
    }
}
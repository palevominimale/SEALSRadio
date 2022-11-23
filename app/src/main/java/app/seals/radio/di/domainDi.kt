package app.seals.radio.di

import app.seals.radio.domain.usecases.*
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
        SetFilterUseCase(
            filterControls = get()
        )
    }

    single {
        UnsetFilterUseCase(
            filterControls = get()
        )
    }

    single {
        GetListBySearchUseCase(
            repo = get()
        )
    }
}
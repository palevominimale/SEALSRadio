package app.seals.radio.di

import app.seals.radio.domain.usecases.GetTopListUseCase
import org.koin.dsl.module

val domainDi = module {
    factory {
        GetTopListUseCase(
            repo = get()
        )
    }
}
package app.seals.radio.di

import app.seals.radio.main.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiDi = module {
    viewModel {
        MainActivityViewModel(
            getTop = get(),
            getByFilter = get(),
            prefs = get()
        )
    }
}
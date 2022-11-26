package app.seals.radio.domain.usecases.prefs

import app.seals.radio.domain.interfaces.PrefsRepo
import app.seals.radio.domain.models.FilterOptions

class PreferencesInteractionsUseCase(
    private val prefs: PrefsRepo
) {
    fun setFilter(filterOptions: FilterOptions) = prefs.setFilter(filterOptions)
    fun clearFilter() = prefs.clearFilter()
    fun getFilter() = prefs.getFilter()
    fun setLastSearch(search: String) = prefs.setLastSearch(search)
    fun getLastSearch() = prefs.getLastSearch()
}
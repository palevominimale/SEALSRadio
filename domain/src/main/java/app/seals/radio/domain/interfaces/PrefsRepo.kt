package app.seals.radio.domain.interfaces

import app.seals.radio.domain.models.FilterOptions

interface PrefsRepo {
    fun setFilter(options: FilterOptions)
    fun getFilter() : FilterOptions
    fun clearFilter()
    fun setLastSearch(search: String)
    fun getLastSearch() : String
}
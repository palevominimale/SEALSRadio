package app.seals.radio.domain.interfaces

import app.seals.radio.domain.models.FilterOptions

interface FilterControls {
    fun setFilter(filter: FilterOptions)
    fun unsetFilter()
}
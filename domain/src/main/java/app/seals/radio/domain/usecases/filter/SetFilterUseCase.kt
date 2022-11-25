package app.seals.radio.domain.usecases.filter

import app.seals.radio.domain.interfaces.FilterControls
import app.seals.radio.domain.models.FilterOptions

class SetFilterUseCase(
    private val filterControls: FilterControls
) {
    fun execute(filter: FilterOptions) {
        filterControls.setFilter(filter)
    }
}
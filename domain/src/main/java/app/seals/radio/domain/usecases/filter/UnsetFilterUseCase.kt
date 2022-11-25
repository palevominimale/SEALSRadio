package app.seals.radio.domain.usecases.filter

import app.seals.radio.domain.interfaces.FilterControls

class UnsetFilterUseCase(
    private val filterControls: FilterControls
) {
    fun execute() {
        filterControls.unsetFilter()
    }
}
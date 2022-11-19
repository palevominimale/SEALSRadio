package app.seals.radio.domain.usecases

import app.seals.radio.domain.interfaces.FilterControls

class UnsetFilterUseCase(
    private val filterControls: FilterControls
) {
    fun execute() {
        filterControls.unsetFilter()
    }
}
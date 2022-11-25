package app.seals.radio.domain.usecases.local_storage

import app.seals.radio.domain.interfaces.LocalRepo
import app.seals.radio.entities.responses.StationModel

class CurrentListUseCase(
    private val repo: LocalRepo
) {

    fun get() = repo.loadCurrentList()
    fun clear() = repo.clearCurrentList()
    fun set(list: List<StationModel>) {
        repo.saveCurrentList(list)
    }
}
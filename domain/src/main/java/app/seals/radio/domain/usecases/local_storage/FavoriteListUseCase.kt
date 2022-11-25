package app.seals.radio.domain.usecases.local_storage

import app.seals.radio.domain.interfaces.LocalRepo
import app.seals.radio.entities.responses.StationModel

class FavoriteListUseCase(
    private val repo: LocalRepo
) {

    fun add(station: StationModel) = repo.saveFavorite(station)
    fun clear() = repo.clearFavoritesList()
    fun getList() = repo.loadFavoritesList()
    fun delete(uuid: String) = repo.deleteFavorite(uuid)
}
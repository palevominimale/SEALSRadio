package app.seals.radio.domain.interfaces

import app.seals.radio.entities.responses.StationModel

interface LocalRepo {
    fun saveCurrentList(list: List<StationModel>)
    fun loadCurrentList() : List<StationModel>
    fun clearCurrentList()

    fun loadFavoritesList() : List<StationModel>
    fun clearFavoritesList()
    fun saveFavorite(station: StationModel)
    fun deleteFavorite(uuid: String)
}
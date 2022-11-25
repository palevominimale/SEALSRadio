package app.seals.radio.data.repos

import android.content.Context
import app.seals.radio.data.room.*
import app.seals.radio.domain.interfaces.LocalRepo
import app.seals.radio.entities.responses.StationModel

class LocalRepoImpl(
    private val context: Context
): LocalRepo {

    private var db : RoomDao = RoomDB.getInstance(context)?.dao()!!

    override fun saveCurrentList(list: List<StationModel>) {
        db.saveCurrentList(list.mapToCurrent())
    }

    override fun loadCurrentList(): List<StationModel> {
        return db.loadCurrentList().mapToDomain()
    }

    override fun clearCurrentList() {
        db.clearCurrentList()
    }

    override fun loadFavoritesList(): List<StationModel> {
        return db.loadFavoritesList().mapToDomain()
    }

    override fun clearFavoritesList() {
        db.clearFavoritesList()
    }

    override fun saveFavorite(station: StationModel) {
        db.saveFavorite(station.mapToFavorite())
    }

    override fun deleteFavorite(uuid: String) {
        db.deleteFavorite(uuid)
    }

}
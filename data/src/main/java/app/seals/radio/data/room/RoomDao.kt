package app.seals.radio.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.seals.radio.data.models.StationModelDataCurrent
import app.seals.radio.data.models.StationModelDataFavorites

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrentList(list: List<StationModelDataCurrent>)

    @Query("SELECT * FROM Current")
    fun loadCurrentList() : List<StationModelDataCurrent>

    @Query("DELETE FROM Current")
    fun clearCurrentList()

    @Query("SELECT * FROM Favorites")
    fun loadFavoritesList() : List<StationModelDataFavorites>

    @Query("DELETE FROM Favorites")
    fun clearFavoritesList()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFavorite(station: StationModelDataFavorites)

    @Query("DELETE FROM Favorites WHERE stationuuid LIKE :uuid")
    fun deleteFavorite(uuid: String)

}
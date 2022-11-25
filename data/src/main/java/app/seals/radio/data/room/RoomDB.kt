package app.seals.radio.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.seals.radio.data.models.StationModelDataCurrent
import app.seals.radio.data.models.StationModelDataFavorites

@Database(entities = [StationModelDataCurrent::class, StationModelDataFavorites::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {
    abstract fun dao(): RoomDao

    companion object {
        private var INSTANCE: RoomDB? = null
        fun getInstance(context: Context): RoomDB? {
            if (INSTANCE == null) synchronized(RoomDB::class) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    RoomDB::class.java,
                    "stations.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}
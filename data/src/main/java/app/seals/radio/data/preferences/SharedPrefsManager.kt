package app.seals.radio.data.preferences

import android.content.Context
import app.seals.radio.data.R
import app.seals.radio.domain.models.FilterOptions
import com.google.gson.Gson
import java.lang.StringBuilder

class SharedPrefsManager(
    context: Context
) {

    private val sharedPreference =  context.getSharedPreferences(context.getString(R.string.prefs_filter),Context.MODE_PRIVATE)
    private var editor = sharedPreference.edit()
    private val gson = Gson()

    fun setFilter(filter: FilterOptions) {
        val f = gson.toJson(filter)
        editor.putString("filter", f)
        editor.commit()
    }

    fun getFilter() : FilterOptions {
        val filter = sharedPreference.getString("filter", "")
        return gson.fromJson(filter, FilterOptions::class.java)
    }

    fun setLastSearch(search: String) {
        editor.putString("last_search", search)
        editor.commit()
    }

    fun getLastSearch() : String {
        return sharedPreference.getString("last_search", "") ?: ""
    }

    fun addFavorite(uuid: String) {
        val strList = sharedPreference.getString("favorites", "") ?: ""
        val list = strList.split(",").toMutableList()
        list.add(uuid)
        val newStrList = list.joinToString(",")
        editor.putString("favorites", newStrList)
        editor.commit()
    }

    fun delFavorite(uuid: String) {
        val strList = sharedPreference.getString("favorites", "") ?: ""
        val list = strList.split(",").toMutableList()
        list.remove(uuid)
        val newStrList = list.joinToString(",")
        editor.putString("favorites", newStrList)
        editor.commit()
    }

    fun getFavorites() : List<String> {
        val list = sharedPreference.getString("favorites", "") ?: ""
        return list.split(",")
    }
}
package app.seals.radio.data.preferences

import android.content.Context
import app.seals.radio.data.R
import app.seals.radio.domain.interfaces.PrefsRepo
import app.seals.radio.domain.models.FilterOptions
import com.google.gson.Gson

class SharedPrefsManager(
    context: Context
) : PrefsRepo {

    private val sharedPreference =  context.getSharedPreferences(context.getString(R.string.prefs_filter),Context.MODE_PRIVATE)
    private var editor = sharedPreference.edit()
    private val gson = Gson()

    override fun setFilter(options: FilterOptions) {
        val f = gson.toJson(options)
        editor.putString("filter", f)
        editor.commit()
    }

    override fun clearFilter() {
        val f = gson.toJson(FilterOptions())
        editor.putString("filter", f)
        editor.commit()
    }

    override fun getFilter() : FilterOptions {
        val filter = sharedPreference.getString("filter", "")
        return gson.fromJson(filter, FilterOptions::class.java)
    }

    override fun setLastSearch(search: String) {
        editor.putString("last_search", search)
        editor.commit()
    }

    override fun getLastSearch() : String {
        return sharedPreference.getString("last_search", "") ?: ""
    }
}
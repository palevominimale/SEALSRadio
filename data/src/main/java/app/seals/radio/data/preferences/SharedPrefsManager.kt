package app.seals.radio.data.preferences

import android.content.Context
import app.seals.radio.data.R
import app.seals.radio.domain.models.FilterOptions
import com.google.gson.Gson

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
}
package app.seals.redio.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CountryModel(
    @SerializedName("name"         ) var name         : String? = null,
    @SerializedName("iso_3166_1"   ) var iso31661     : String? = null,
    @SerializedName("stationcount" ) var stationcount : Int?    = null
) : Serializable

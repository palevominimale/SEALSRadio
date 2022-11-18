package app.seals.redio.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TagModel(
    @SerializedName("name"         ) val name         : String? = null,
    @SerializedName("stationcount" ) val stationcount : Int?    = null
) : Serializable
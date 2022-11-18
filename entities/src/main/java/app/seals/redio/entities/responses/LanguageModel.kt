package app.seals.redio.entities.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LanguageModel(
    @SerializedName("name"         ) val name         : String? = null,
    @SerializedName("iso_639"      ) val iso639       : String? = null,
    @SerializedName("stationcount" ) val stationcount : Int?    = null
) : Serializable
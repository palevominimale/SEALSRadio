package app.seals.radio.data.api

import app.seals.radio.entities.responses.CountryModel
import app.seals.radio.entities.responses.LanguageModel
import app.seals.radio.entities.responses.StationModel
import app.seals.radio.entities.responses.TagModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiRequest {

    @GET("stations/topclick/50")
    fun getListTop(): Call<List<StationModel>>

    @GET("stations/topclick/{n}")
    fun getListTop(@Path("n") num: Int): Call<List<StationModel>>

    @GET("stations/search?")
    fun getListWithFilter(
        @Query("country") country: String,
        @Query("tagList") tagList: String,
        @Query("language") language: String,
        @Query("limit") limit: Int = 50
    ): Call<List<StationModel>>

    @GET("stations/bylanguage/{lang}")
    fun getListByLang(@Path("lang") lang: String): Call<List<StationModel>>

    @GET("stations/bycountry/{country}")
    fun getListByCountry(@Path("country") country: String): Call<List<StationModel>>

    @GET("stations/bytag/{tag}")
    fun getListByTag(@Path("tag") tag: String): Call<List<StationModel>>

    @GET("languages")
    fun getLangList(): Call<List<LanguageModel>>

    @GET("tags")
    fun getTagsList(): Call<List<TagModel>>

    @GET("countries")
    fun getCountryList(): Call<List<CountryModel>>

    companion object {
        const val BASE_URL = """http://89.58.16.19//json/"""
    }
}
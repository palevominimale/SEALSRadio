package app.seals.radio.data.api

import app.seals.radio.data.api.ApiHandler.handleApi
import app.seals.radio.data.preferences.SharedPrefsManager
import app.seals.radio.entities.api.ApiResult
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkRequest(
    private val prefs: SharedPrefsManager
) {

    private val retrofit: ApiRequest by lazy {
        Retrofit.Builder()
            .baseUrl(ApiRequest.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)
    }

    private suspend fun execute(request: () -> Call<*>) : ApiResult {
        return when(val res = handleApi { request.invoke().execute() } ) {
            is ApiResult.ApiSuccess -> ApiResult.ApiSuccess(data = res.data)
            is ApiResult.ApiError -> ApiResult.ApiError(code = res.code, message = res.message)
            is ApiResult.ApiException -> ApiResult.ApiException(e = res.e)
        }
    }

    suspend fun getTop() : ApiResult {
        return execute { retrofit.getListTop() }
    }

    suspend fun getTop(num: Int) : ApiResult {
        return execute { retrofit.getListTop(num) }
    }

    suspend fun getListWithFilter() : ApiResult {
        val filter = prefs.getFilter()
        return execute { retrofit.getListWithFilter(
            country = filter.country ?: "",
            tagList = filter.tags?.lowercase() ?: "",
            language = filter.language?.lowercase() ?: ""
        ) }
    }

    suspend fun getListWithSearchByName() : ApiResult {
        val search = prefs.getLastSearch()
        return execute { retrofit.getListWithSearchByName(search) }
    }
}
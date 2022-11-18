package app.seals.radio.entities.api

sealed interface ApiResult {
    data class ApiSuccess(val data: List<Any>) : ApiResult
    data class ApiError(val code: Int? = null, val message: String? = null) : ApiResult
    data class ApiException(val e: Throwable? = null, val message: String? = null) : ApiResult
}

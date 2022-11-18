package app.seals.radio.domain

import app.seals.redio.entities.api.ApiResult

interface RemoteRepo {
    suspend fun getTopList() : ApiResult
    suspend fun getTopList(num: Int) : ApiResult
}
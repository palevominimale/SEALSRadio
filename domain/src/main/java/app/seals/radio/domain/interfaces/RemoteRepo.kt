package app.seals.radio.domain.interfaces

import app.seals.radio.entities.api.ApiResult

interface RemoteRepo {
    suspend fun getTopList() : ApiResult
    suspend fun getTopList(num: Int) : ApiResult
}
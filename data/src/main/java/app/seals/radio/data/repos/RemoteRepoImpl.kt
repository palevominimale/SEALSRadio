package app.seals.radio.data.repos

import app.seals.radio.data.api.NetworkRequest
import app.seals.radio.domain.RemoteRepo
import app.seals.redio.entities.api.ApiResult

class RemoteRepoImpl(
    private val request: NetworkRequest
) : RemoteRepo {
    override suspend fun getTopList(): ApiResult {
        return request.getTop()
    }

    override suspend fun getTopList(num: Int): ApiResult {
        return request.getTop(num)
    }
}
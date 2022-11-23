package app.seals.radio.data.repos

import app.seals.radio.data.api.NetworkRequest
import app.seals.radio.domain.interfaces.FilterControls
import app.seals.radio.domain.interfaces.RemoteRepo
import app.seals.radio.domain.models.FilterOptions
import app.seals.radio.entities.api.ApiResult

class RemoteRepoImpl(
    private val request: NetworkRequest
) : RemoteRepo {

    override suspend fun getTopList(): ApiResult {
        return request.getTop()
    }

    override suspend fun getTopList(num: Int): ApiResult {
        return request.getTop(num)
    }

    override suspend fun getListWithFilter() : ApiResult {
        return request.getListWithFilter()
    }

}
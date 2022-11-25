package app.seals.radio.domain.usecases.api_ops

import app.seals.radio.domain.interfaces.RemoteRepo
import app.seals.radio.entities.api.ApiResult

class GetTopListUseCase(
    private val repo: RemoteRepo
) {
    suspend fun execute() : ApiResult {
        return repo.getTopList()
    }
}
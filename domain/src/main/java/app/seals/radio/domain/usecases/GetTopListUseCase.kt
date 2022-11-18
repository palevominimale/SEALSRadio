package app.seals.radio.domain.usecases

import app.seals.radio.domain.interfaces.RemoteRepo
import app.seals.redio.entities.api.ApiResult

class GetTopListUseCase(
    private val repo: RemoteRepo
) {
    suspend fun execute() : ApiResult {
        return repo.getTopList()
    }
}
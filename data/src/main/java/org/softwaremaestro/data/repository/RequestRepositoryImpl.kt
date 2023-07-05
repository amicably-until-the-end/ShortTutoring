package org.softwaremaestro.data.repository

import org.softwaremaestro.domain.model.vo.RequestVO
import org.softwaremaestro.domain.repository.RequestRepository
import javax.inject.Inject

class RequestRepositoryImpl @Inject constructor(): RequestRepository {
    override suspend fun sendRequest(request: RequestVO) {
        TODO("Not yet implemented")
    }

    override suspend fun getRequests(): List<RequestVO> {
        TODO("Not yet implemented")
    }
}
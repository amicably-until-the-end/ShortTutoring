package org.softwaremaestro.domain.repository

import org.softwaremaestro.domain.model.vo.RequestVO

interface RequestRepository {
    suspend fun sendRequest(request: RequestVO)
    suspend fun getRequests(): List<RequestVO>
}
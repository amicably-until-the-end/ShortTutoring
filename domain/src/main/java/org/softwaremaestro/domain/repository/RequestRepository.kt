package org.softwaremaestro.domain.repository

import org.softwaremaestro.domain.model.Request

interface RequestRepository {
    suspend fun sendRequest(request: Request)
    suspend fun receiveRequests(): List<Request>
}
package org.softwaremaestro.domain.repository

import org.softwaremaestro.domain.model.Response

interface ResponseRepository {
    suspend fun sendResponse(response: Response)
}
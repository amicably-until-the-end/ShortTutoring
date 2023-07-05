package org.softwaremaestro.domain.repository

import org.softwaremaestro.domain.model.vo.ResponseVO

interface ResponseRepository {
    suspend fun sendResponse(responseVO: ResponseVO)
}
package org.softwaremaestro.domain.repository

import org.softwaremaestro.domain.model.vo.ResponseVO

interface ResponseRepository {
    fun sendResponse(responseVO: ResponseVO)
}
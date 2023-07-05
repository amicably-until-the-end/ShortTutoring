package org.softwaremaestro.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.model.vo.ResponseVO
import org.softwaremaestro.domain.repository.ResponseRepository

class SendResponseUseCase(private val responseRepository: ResponseRepository) {

    operator fun invoke(
        scope: CoroutineScope,
        responseVO: ResponseVO
    ) {
        scope.launch(Dispatchers.IO) {
            responseRepository.sendResponse(responseVO)
        }
    }
}
package org.softwaremaestro.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.model.vo.RequestVO
import org.softwaremaestro.domain.repository.RequestRepository
import kotlin.coroutines.CoroutineContext

class SendRequestUseCase(private val requestRepository: RequestRepository) {

    operator fun invoke(
        scope: CoroutineScope,
        request: RequestVO
    ) {
        scope.launch(Dispatchers.IO) {
            requestRepository.sendRequest(request)
        }
    }
}
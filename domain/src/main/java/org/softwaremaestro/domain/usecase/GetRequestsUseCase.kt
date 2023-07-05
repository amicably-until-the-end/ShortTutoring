package org.softwaremaestro.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.model.vo.RequestVO
import org.softwaremaestro.domain.repository.RequestRepository
import javax.inject.Inject

class GetRequestsUseCase @Inject constructor(private val requestRepository: RequestRepository) {

    operator fun invoke(
        scope: CoroutineScope,
        onRespond: (List<RequestVO>) -> Unit
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                requestRepository.getRequests()
            }
            onRespond(deferred.await())
        }
    }
}
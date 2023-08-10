package org.softwaremaestro.domain.offer_remove.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.offer_remove.OfferRemoveRepository
import javax.inject.Inject

class OfferRemoveUseCase @Inject constructor(private val offerRemoveRepository: OfferRemoveRepository) {

    suspend fun execute(questionId: String): Flow<BaseResult<String, String>> {
        return offerRemoveRepository.removeOffer(questionId)
    }
}
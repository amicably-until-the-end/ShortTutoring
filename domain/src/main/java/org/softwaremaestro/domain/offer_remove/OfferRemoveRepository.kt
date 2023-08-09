package org.softwaremaestro.domain.offer_remove

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult

interface OfferRemoveRepository {
    suspend fun removeOffer(questionId: String): Flow<BaseResult<String, String>>
}
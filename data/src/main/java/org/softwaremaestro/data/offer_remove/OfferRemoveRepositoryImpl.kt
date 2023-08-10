package org.softwaremaestro.data.offer_remove

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.offer_remove.remote.OfferRemoveApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.offer_remove.OfferRemoveRepository
import org.softwaremaestro.domain.offer_remove.SUCCESS_OFFER_REMOVE
import javax.inject.Inject

class OfferRemoveRepositoryImpl @Inject constructor(private val offerRemoveApi: OfferRemoveApi) :
    OfferRemoveRepository {

    override suspend fun removeOffer(questionId: String): Flow<BaseResult<String, String>> {
        return flow {
            val response = offerRemoveApi.removeOffer(questionId)
            val body = response.body()
            if (body?.success == true) {
                emit(BaseResult.Success(SUCCESS_OFFER_REMOVE))
            } else {
                val errorString =
                    "error in ${this@OfferRemoveRepositoryImpl::class.java.name}\n" +
                            "message: ${response.message()}"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
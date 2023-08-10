package org.softwaremaestro.data.offer_remove.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface OfferRemoveApi {
    @POST("/teacher/offer/remove/{questionId}")
    suspend fun removeOffer(@Path("questionId") questionId: String): Response<WrappedResponse<Unit>>
}
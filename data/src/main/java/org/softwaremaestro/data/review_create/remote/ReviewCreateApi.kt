package org.softwaremaestro.data.review_create.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.review_create.model.ReviewReqDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewCreateApi {

    @POST("/tutoring/review/create/{tutoringId}")
    suspend fun createReview(
        @Path("tutoringId") tutoringId: String,
        @Body reviewReqDto: ReviewReqDto
    ): Response<WrappedResponse<Unit>>


}
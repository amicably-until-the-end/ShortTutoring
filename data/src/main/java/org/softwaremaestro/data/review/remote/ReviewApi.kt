package org.softwaremaestro.data.review.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.review.model.ReviewCreateReqDto
import org.softwaremaestro.data.review.model.ReviewGetResDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApi {

    @GET("/teacher/review/list/{teacherId}")
    suspend fun getReview(
        @Path("teacherId") teacherId: String
    ): Response<WrappedResponse<ReviewGetResDto>>


    @POST("/tutoring/review/create/{tutoringId}")
    suspend fun createReview(
        @Path("tutoringId") tutoringId: String,
        @Body reviewCreateReqDto: ReviewCreateReqDto
    ): Response<WrappedResponse<Unit>>


}
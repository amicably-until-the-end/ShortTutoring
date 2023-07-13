package org.softwaremaestro.data.question_check.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.question_check.model.QuestionCheckRequestDto
import org.softwaremaestro.data.question_check.model.QuestionCheckResponseDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestionCheckApi {
    @POST("/response/check/{id}")
    suspend fun checkQuestion(@Path("id") questionCheckRequestDto: QuestionCheckRequestDto): Response<WrappedResponse<QuestionCheckResponseDto>>
}
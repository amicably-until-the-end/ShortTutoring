package org.softwaremaestro.data.question_get.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.question_get.model.QuestionGetResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface QuestionGetApi {
    @GET("/question/list?status=pending")
    suspend fun getQuestions(): Response<WrappedListResponse<QuestionGetResponseDto>>
}
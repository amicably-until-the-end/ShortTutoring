package org.softwaremaestro.data.question_get.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.question_get.model.QuestionsGetResultDto
import retrofit2.Response
import retrofit2.http.GET

interface QuestionGetApi {
    @GET("/request/list")
    suspend fun getQuestions(): Response<WrappedListResponse<QuestionsGetResultDto>>
}
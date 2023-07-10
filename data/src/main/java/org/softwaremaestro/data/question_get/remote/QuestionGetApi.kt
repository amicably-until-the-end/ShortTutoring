package org.softwaremaestro.data.question_get.remote

import org.softwaremaestro.data.common.module.WrappedResponse
import org.softwaremaestro.data.question_get.model.QuestionGetResultDto
import retrofit2.Response
import retrofit2.http.POST

interface QuestionGetApi {
    @POST("/request")
    suspend fun getQuestion(): Response<WrappedResponse<QuestionGetResultDto>>
}
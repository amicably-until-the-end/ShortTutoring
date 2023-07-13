package org.softwaremaestro.data.question_get.remote

import org.softwaremaestro.data.question_get.model.QuestionsGetResultDto
import retrofit2.Response
import retrofit2.http.GET

interface QuestionGetApi {
    @GET("/request")
    suspend fun getQuestions(): Response<List<QuestionsGetResultDto>>
}
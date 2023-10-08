package org.softwaremaestro.data.question_get.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.question_get.model.QuestionsGetResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuestionGetApi {
    @GET("/teacher/question/list?status=pending")
    suspend fun getQuestions(): Response<WrappedListResponse<QuestionsGetResultDto>>

    @GET("/question/info/{questionId}")
    suspend fun getQuestionInfo(@Path("questionId") questionId: String): Response<WrappedResponse<QuestionsGetResultDto>>

    @GET("/student/question/list/my")
    suspend fun getMyQuestionList(
        @Query("status") status: String,
        @Query("type") type: String
    ): Response<WrappedListResponse<QuestionsGetResultDto>>

}
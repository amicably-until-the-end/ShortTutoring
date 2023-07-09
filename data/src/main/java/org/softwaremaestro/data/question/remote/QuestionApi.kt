package org.softwaremaestro.data.question.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.question.dto.QuestionDto
import org.softwaremaestro.data.question.dto.QuestionListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface QuestionApi {
    @POST
    suspend fun getQuestions(@Body questionDto: QuestionDto) : Response<WrappedResponse<QuestionListDto>>

}
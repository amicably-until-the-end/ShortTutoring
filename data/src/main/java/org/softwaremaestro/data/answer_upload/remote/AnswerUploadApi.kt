package org.softwaremaestro.data.answer_upload.remote

import org.softwaremaestro.data.answer_upload.model.AnswerUploadRequestDto
import org.softwaremaestro.data.answer_upload.model.AnswerUploadResultDto
import org.softwaremaestro.data.answer_upload.model.TeacherDto
import org.softwaremaestro.data.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AnswerUploadApi {
    @GET("/teacher/offer/append/{questionId}")
    suspend fun uploadAnswer(
        @Path("questionId") requestId: String
    ): Response<WrappedResponse<AnswerUploadResultDto>>
}
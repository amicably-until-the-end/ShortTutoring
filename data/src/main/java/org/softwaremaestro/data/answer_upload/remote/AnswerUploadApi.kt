package org.softwaremaestro.data.answer_upload.remote

import org.softwaremaestro.data.answer_upload.model.AnswerUploadRequestDto
import org.softwaremaestro.data.answer_upload.model.AnswerUploadResultDto
import org.softwaremaestro.data.answer_upload.model.TeacherDto
import org.softwaremaestro.data.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AnswerUploadApi {
    @POST("/response/create/{requestId}")
    suspend fun uploadAnswer(
        @Path("requestId") requestId: String,
        @Body teacher: TeacherDto
    ): Response<WrappedResponse<AnswerUploadResultDto>>
}
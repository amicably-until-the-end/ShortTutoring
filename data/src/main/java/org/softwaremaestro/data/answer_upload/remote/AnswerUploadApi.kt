package org.softwaremaestro.data.answer_upload.remote

import org.softwaremaestro.data.answer_upload.model.AnswerUploadRequestDto
import org.softwaremaestro.data.answer_upload.model.AnswerUploadResultDto
import org.softwaremaestro.data.answer_upload.model.TeacherDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AnswerUploadApi {
    @PATCH("/response/{id}")
    suspend fun uploadAnswer(
        @Path("id") id: String,
        @Body teacher: TeacherDto
    ): Response<AnswerUploadResultDto>
}
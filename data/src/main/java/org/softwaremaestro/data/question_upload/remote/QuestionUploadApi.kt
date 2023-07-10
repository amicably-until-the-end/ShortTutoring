package org.softwaremaestro.data.question_upload.remote

import org.softwaremaestro.data.common.module.WrappedResponse
import org.softwaremaestro.data.question_upload.model.QuestionUploadRequestDto
import org.softwaremaestro.data.question_upload.model.QuestionUploadResultDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface QuestionUploadApi {
    @POST("post/test")
    suspend fun uploadQuestion(@Body questionUploadRequestDto: QuestionUploadRequestDto): Response<WrappedResponse<QuestionUploadResultDto>>

}
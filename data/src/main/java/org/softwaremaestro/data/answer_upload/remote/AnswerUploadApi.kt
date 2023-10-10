package org.softwaremaestro.data.answer_upload.remote

import org.softwaremaestro.data.answer_upload.model.AnswerUploadResDto
import org.softwaremaestro.data.answer_upload.model.StudentPickReqDto
import org.softwaremaestro.data.answer_upload.model.StudentPickResultDto
import org.softwaremaestro.data.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AnswerUploadApi {
    @GET("/teacher/offer/append/{questionId}")
    suspend fun uploadAnswer(
        @Path("questionId") requestId: String
    ): Response<WrappedResponse<AnswerUploadResDto>>

    @POST("/tutoring/appoint/{questionId}")
    suspend fun pickStudent(
        @Path("questionId") questionId: String, @Body body: StudentPickReqDto
    ): Response<WrappedResponse<StudentPickResultDto>>

    @GET("/tutoring/decline/{chattingId}")
    suspend fun declineStudent(
        @Path("chattingId") chattingId: String
    ): Response<WrappedResponse<StudentPickResultDto>>
}
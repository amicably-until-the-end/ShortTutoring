package org.softwaremaestro.data.user_follow.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.question_upload.model.PickTeacherReqDto
import org.softwaremaestro.data.question_upload.model.PickTeacherResDto
import org.softwaremaestro.data.question_upload.model.QuestionUploadRequestDto
import org.softwaremaestro.data.question_upload.model.QuestionUploadResultDto
import org.softwaremaestro.data.question_upload.model.TeacherDto
import org.softwaremaestro.data.question_upload.model.TeacherListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestionUploadApi {
    @POST("/student/question/create/normal")
    suspend fun uploadQuestion(
        @Body questionUploadRequestDto: QuestionUploadRequestDto
    ): Response<WrappedResponse<QuestionUploadResultDto>>

    @GET("/student/offer/teachers/{questionId}")
    suspend fun getTeacherList(@Path("questionId") questionId: String): Response<WrappedResponse<TeacherListDto>>

    @POST("/student/offer/accept/{questionId}")
    suspend fun pickTeacher(
        @Path("questionId") questionId: String,
        @Body pickTeacherReqDto: PickTeacherReqDto
    ): Response<WrappedResponse<Unit>>


}
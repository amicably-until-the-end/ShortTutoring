package org.softwaremaestro.data.question_upload.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.question_upload.model.PickTeacherReqDto
import org.softwaremaestro.data.question_upload.model.PickTeacherResDto
import org.softwaremaestro.data.question_upload.model.QuestionUploadRequestDto
import org.softwaremaestro.data.question_upload.model.QuestionUploadResultDto
import org.softwaremaestro.data.question_upload.model.TeacherDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestionUploadApi {
    @POST("/request/create/{id}")
    suspend fun uploadQuestion(
        @Path("id") studentId: String,
        @Body questionUploadRequestDto: QuestionUploadRequestDto
    ): Response<WrappedResponse<QuestionUploadResultDto>>

    @GET("/response/teacherList/{questionId}")
    suspend fun getTeacherList(@Path("questionId") questionId: String): Response<WrappedListResponse<TeacherDto>>

    @POST("/response/select")
    suspend fun pickTeacher(@Body pickTeacherReqDto: PickTeacherReqDto): Response<WrappedResponse<PickTeacherResDto>>


}
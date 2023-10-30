package org.softwaremaestro.data.login.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.login.model.RegisterResDto
import org.softwaremaestro.data.login.model.StudentRegisterReqDto
import org.softwaremaestro.data.login.model.TeacherRegisterReqDto
import org.softwaremaestro.data.login.model.WithdrawResDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RegisterApi {
    @POST("/student/signup")
    suspend fun registerStudent(@Body studentRegisterReqDto: StudentRegisterReqDto): Response<WrappedResponse<RegisterResDto>>

    @POST("/teacher/signup")
    suspend fun registerTeacher(@Body teacherRegisterReqDto: TeacherRegisterReqDto): Response<WrappedResponse<RegisterResDto>>

    @GET("/user/withdraw")
    suspend fun withdraw(): Response<WrappedResponse<WithdrawResDto>>
}
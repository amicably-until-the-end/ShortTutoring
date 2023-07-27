package org.softwaremaestro.data.login.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.login.model.RegisterResDto
import org.softwaremaestro.data.login.model.StudentRegisterReqDto
import org.softwaremaestro.data.login.model.TeacherRegisterReqDto
import org.softwaremaestro.domain.login.entity.StudentRegisterVO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("/user/register")
    suspend fun registerStudent(@Body studentRegisterReqDto: StudentRegisterReqDto): Response<WrappedResponse<RegisterResDto>>

    @POST("/user/register")
    suspend fun registerTeacher(@Body teacherRegisterReqDto: TeacherRegisterReqDto): Response<WrappedResponse<RegisterResDto>>
}
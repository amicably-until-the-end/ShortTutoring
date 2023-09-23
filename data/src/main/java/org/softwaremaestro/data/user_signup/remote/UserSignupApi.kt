package org.softwaremaestro.data.user_signup.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.user_signup.model.StudentSignupRequestDto
import org.softwaremaestro.data.user_signup.model.StudentSignupResultDto
import org.softwaremaestro.data.user_signup.model.TeacherSignupRequestDto
import org.softwaremaestro.data.user_signup.model.TeacherSignupResultDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface UserSignupApi {

    @GET("/student/signup")
    suspend fun signupStudent(@Body studentSignupRequestDto: StudentSignupRequestDto): Response<WrappedResponse<StudentSignupResultDto>>

    @GET("/teacher/signup")
    suspend fun signupTeacher(@Body teacherSignupRequestDto: TeacherSignupRequestDto): Response<WrappedResponse<TeacherSignupResultDto>>
}
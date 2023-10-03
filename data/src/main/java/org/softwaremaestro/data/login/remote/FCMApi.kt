package org.softwaremaestro.data.login.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.login.model.LoginReqDto
import org.softwaremaestro.data.login.model.LoginResDto
import org.softwaremaestro.data.login.model.RegisterFCMTokenReqDto
import org.softwaremaestro.data.login.model.UserInfoResDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FCMApi {
    @POST("/user/login")
    suspend fun login(
        @Body loginReqDto: LoginReqDto
    ): Response<WrappedResponse<LoginResDto>>

    @GET("/user/profile")
    suspend fun getUserInfo(
    ): Response<WrappedResponse<UserInfoResDto>>

    @POST("/user/fcmToken")
    suspend fun registerFCMToken(
        @Body registerFCMTokenReqDto: RegisterFCMTokenReqDto
    ): Response<WrappedResponse<String>>
}
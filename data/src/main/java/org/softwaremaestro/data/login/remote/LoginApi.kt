package org.softwaremaestro.data.login.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.login.model.LoginReqDto
import org.softwaremaestro.data.login.model.UserInfoResDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @GET("/user/login")
    suspend fun login(
        @Header("vendor") vendor: String,
        @Header("Authorization") authCode: String
    ): Response<WrappedResponse<UserInfoResDto>>

}
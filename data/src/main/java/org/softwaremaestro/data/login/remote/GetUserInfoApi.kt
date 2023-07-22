package org.softwaremaestro.data.login.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.login.model.UserInfoReqDto
import org.softwaremaestro.data.login.model.UserInfoResDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GetUserInfoApi {
    @POST("/user/getInfo")
    suspend fun getUserInfo(): Response<WrappedResponse<UserInfoResDto>>
    
}
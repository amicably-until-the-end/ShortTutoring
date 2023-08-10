package org.softwaremaestro.data.my_profile_get.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.my_profile_get.model.MyProfileGetResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface MyProfileGetApi {
    @GET("/user/profile")
    suspend fun getMyProfile(): Response<WrappedResponse<MyProfileGetResponseDto>>
}
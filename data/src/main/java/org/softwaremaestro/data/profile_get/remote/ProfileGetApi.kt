package org.softwaremaestro.data.profile_get.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.profile_get.model.ProfileGetResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileGetApi {
    @GET("/user/profile/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): Response<WrappedResponse<ProfileGetResponseDto>>
}
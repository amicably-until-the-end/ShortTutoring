package org.softwaremaestro.data.profile_get.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.profile_get.model.ProfileGetResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ProfileGetApi {
    @GET("/user/profile")
    suspend fun getProfile(): Response<WrappedResponse<ProfileGetResponseDto>>
}
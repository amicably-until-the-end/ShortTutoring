package org.softwaremaestro.data.profile.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.profile.model.MyProfileResDto
import org.softwaremaestro.data.profile.model.ProfileResDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {
    @GET("/user/profile/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): Response<WrappedResponse<ProfileResDto>>

    @GET("/user/profile/update/")
    suspend fun updateProfile(): Response<WrappedResponse<MyProfileResDto>>

    @GET("/user/profile")
    suspend fun getMyProfile(): Response<WrappedResponse<MyProfileResDto>>
}
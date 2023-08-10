package org.softwaremaestro.data.following_get.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.following_get.model.FollowingGetResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FollowingGetApi {
    @GET("/user/following/{userId}")
    suspend fun getFollowing(@Path("userId") userId: String): Response<WrappedListResponse<FollowingGetResponseDto>>
}
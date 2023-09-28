package org.softwaremaestro.data.follower_get.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.follower_get.model.FollowerGetResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FollowerGetApi {
    @GET("/user/followers/{userId}")
    suspend fun getFollowers(@Path("userId") userId: String): Response<WrappedListResponse<FollowerGetResponseDto>>
}
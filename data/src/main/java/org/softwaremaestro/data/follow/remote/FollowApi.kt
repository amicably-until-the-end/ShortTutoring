package org.softwaremaestro.data.follow.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.follow.model.FollowerGetResponseDto
import org.softwaremaestro.data.follow.model.FollowingGetResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FollowApi {
    @GET("/student/follow/{userId}")
    suspend fun followUser(@Path("userId") userId: String): Response<WrappedResponse<String>>

    @GET("/student/unfollow/{userId}")
    suspend fun unfollowUser(@Path("userId") userId: String): Response<WrappedResponse<String>>

    @GET("/user/followers/{userId}")
    suspend fun getFollowers(@Path("userId") userId: String): Response<WrappedListResponse<FollowerGetResponseDto>>

    @GET("/user/following/{userId}")
    suspend fun getFollowing(@Path("userId") userId: String): Response<WrappedListResponse<FollowingGetResponseDto>>
}
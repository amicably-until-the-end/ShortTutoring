package org.softwaremaestro.data.user_follow.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserFollowApi {

    @GET("/student/follow/{userId}")
    suspend fun followUser(@Path("userId") userId: String): Response<WrappedResponse<String>>

    @GET("/student/unfollow/{userId}")
    suspend fun unfollowUser(@Path("userId") userId: String): Response<WrappedResponse<String>>
}
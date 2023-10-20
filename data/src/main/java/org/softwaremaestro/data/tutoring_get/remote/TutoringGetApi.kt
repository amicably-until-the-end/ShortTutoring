package org.softwaremaestro.data.tutoring_get.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.tutoring_get.model.TutoringGetResDto
import retrofit2.Response
import retrofit2.http.GET

interface TutoringGetApi {
    @GET("/user/tutoring/list")
    suspend fun getTutoring(): Response<WrappedListResponse<TutoringGetResDto>>
}
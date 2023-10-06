package org.softwaremaestro.data.teacher_onlines_get.remote

import org.softwaremaestro.data.common.utils.WrappedListResponse
import org.softwaremaestro.data.teacher_onlines_get.model.TeacherOnlineDto
import retrofit2.Response
import retrofit2.http.GET

interface TeacherOnlinesGetApi {
    @GET("/user/list/teacher/online")
    suspend fun getTeacherOnlines(): Response<WrappedListResponse<TeacherOnlineDto>>
}
package org.softwaremaestro.data.best_teacher_get.remote

import org.softwaremaestro.data.best_teacher_get.model.BestTeacherGetResDto
import org.softwaremaestro.data.common.utils.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET

interface BestTeacherGetApi {
    @GET("/user/list/teacher/best")
    suspend fun getBestTeacher(): Response<WrappedListResponse<BestTeacherGetResDto>>
}
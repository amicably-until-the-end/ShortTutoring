package org.softwaremaestro.data.schedule_offer.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.question_upload.model.TeacherPickReqDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleOfferApi {
    @POST("/teacher/offer/schedule/{questionId}")
    suspend fun offerSchedule(
        @Path("questionId") questionId: String,
        @Body teacherPickReqDto: TeacherPickReqDto
    ): Response<WrappedResponse<String>>
}
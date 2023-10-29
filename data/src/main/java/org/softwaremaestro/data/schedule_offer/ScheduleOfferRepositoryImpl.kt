package org.softwaremaestro.data.schedule_offer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.question_upload.model.TeacherPickReqDto
import org.softwaremaestro.data.schedule_offer.remote.ScheduleOfferApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.schedule_offer.ScheduleOfferRepository
import javax.inject.Inject

class ScheduleOfferRepositoryImpl @Inject constructor(
    private val scheduleOfferApi: ScheduleOfferApi
) : ScheduleOfferRepository {

    override suspend fun offerSchedule(
        chattingId: String, endTime: String, startTime: String, questionId: String
    ): Flow<BaseResult<String, String>> {
        return flow {
            val dto = TeacherPickReqDto(chattingId, endTime, startTime)
            val response = scheduleOfferApi.offerSchedule(questionId, dto)
            val body = response.body() ?: return@flow
            if (response.isSuccessful && body.success == true) {
                emit(BaseResult.Success("Success"))
            } else {
                emit(BaseResult.Error("error"))
            }
        }
    }
}
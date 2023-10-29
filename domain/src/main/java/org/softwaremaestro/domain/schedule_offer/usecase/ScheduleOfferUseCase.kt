package org.softwaremaestro.domain.schedule_offer.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.schedule_offer.ScheduleOfferRepository
import org.softwaremaestro.domain.schedule_offer.entity.ScheduleOfferResVO
import javax.inject.Inject

class ScheduleOfferUseCase @Inject constructor(private val scheduleOfferRepository: ScheduleOfferRepository) {
    suspend fun execute(
        startTime: String,
        endTime: String,
        chattingId: String,
        questionId: String
    ): Flow<BaseResult<ScheduleOfferResVO, String>> {
        return scheduleOfferRepository.offerSchedule(chattingId, endTime, startTime, questionId)
    }
}
package org.softwaremaestro.domain.schedule_offer

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult


interface ScheduleOfferRepository {
    suspend fun offerSchedule(
        chattingId: String, endTime: String, startTime: String, questionId: String
    ): Flow<BaseResult<String, String>>
}
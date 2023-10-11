package org.softwaremaestro.domain.event.entity

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult

interface EventRepository {
    suspend fun getEvents(): Flow<BaseResult<EventsVO, String>>
}
package org.softwaremaestro.domain.event

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.event.entity.EventsVO

interface EventRepository {
    suspend fun getEvents(): Flow<BaseResult<EventsVO, String>>
}
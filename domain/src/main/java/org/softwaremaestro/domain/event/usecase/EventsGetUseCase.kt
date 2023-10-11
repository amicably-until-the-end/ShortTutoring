package org.softwaremaestro.domain.event.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.event.EventRepository
import org.softwaremaestro.domain.event.entity.EventsVO
import javax.inject.Inject

class EventsGetUseCase @Inject constructor(private val eventRepository: EventRepository) {

    suspend fun execute(): Flow<BaseResult<EventsVO, String>> {
        return eventRepository.getEvents()
    }
}
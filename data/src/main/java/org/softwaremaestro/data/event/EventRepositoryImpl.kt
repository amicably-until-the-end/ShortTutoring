package org.softwaremaestro.data.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.event.model.asDomain
import org.softwaremaestro.data.event.remote.EventApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.event.entity.EventRepository
import org.softwaremaestro.domain.event.entity.EventsVO
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventApi: EventApi) :
    EventRepository {

    override suspend fun getEvents(): Flow<BaseResult<EventsVO, String>> {
        return flow {
            val response = eventApi.getEvents()
            val body = response.body()
            if (body?.success == true) {
                val dto = body.data
                dto?.let { emit(BaseResult.Success(it.asDomain())) }
            } else {
                val errorString = "error in ${this@EventRepositoryImpl::class.java.name}\n" +
                        "message : ${body?.message}"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
package org.softwaremaestro.data.event.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import org.softwaremaestro.data.event.model.EventsResDto
import retrofit2.Response
import retrofit2.http.GET

interface EventApi {
    @GET("/event/list")
    suspend fun getEvents(): Response<WrappedResponse<EventsResDto>>
}
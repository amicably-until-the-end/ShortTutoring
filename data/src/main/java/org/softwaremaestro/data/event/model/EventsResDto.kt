package org.softwaremaestro.data.event.model

import com.google.gson.annotations.SerializedName

data class EventsResDto(
    @SerializedName("count") val count: Int?,
    @SerializedName("events") val events: List<EventDto>?
)

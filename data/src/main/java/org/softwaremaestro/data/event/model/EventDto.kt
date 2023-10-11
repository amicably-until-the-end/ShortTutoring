package org.softwaremaestro.data.event.model

import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("image") val image: String?
)

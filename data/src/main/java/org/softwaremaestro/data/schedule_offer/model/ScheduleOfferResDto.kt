package org.softwaremaestro.data.schedule_offer.model

import com.google.gson.annotations.SerializedName

data class ScheduleOfferResDto(
    @SerializedName("messages") val messages: List<String>?,
    @SerializedName("studentId") val studentId: String?,
    @SerializedName("questionId") val questionId: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("teacherId") val teacherId: String?,
    @SerializedName("id") val id: String?
)
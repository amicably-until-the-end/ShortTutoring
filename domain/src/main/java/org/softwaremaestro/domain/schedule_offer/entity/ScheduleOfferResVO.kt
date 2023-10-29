package org.softwaremaestro.domain.schedule_offer.entity

data class ScheduleOfferResVO(
    val messages: List<String>?,
    val studentId: String?,
    val questionId: String?,
    val status: String?,
    val teacherId: String?,
    val id: String?
)
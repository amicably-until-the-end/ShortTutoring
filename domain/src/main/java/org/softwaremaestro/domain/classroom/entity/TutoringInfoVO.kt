package org.softwaremaestro.domain.classroom.entity

data class TutoringInfoVO(
    val id: String,
    val teacherId: String,
    val studentId: String,
    val status: String? = null,
    val matchedAt: String? = null,
    val reservedStart: String? = null,
    val reservedEnd: String? = null,
)

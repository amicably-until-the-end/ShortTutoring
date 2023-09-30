package org.softwaremaestro.domain.classroom.entity

import java.time.LocalDateTime

data class TutoringInfoVO(
    val id: String,
    val teacherId: String,
    val studentId: String,
    val status: String? = null,
    val matchedAt: String? = null,
    val reservedStart: LocalDateTime? = null,
    val reservedEnd: LocalDateTime? = null,
)

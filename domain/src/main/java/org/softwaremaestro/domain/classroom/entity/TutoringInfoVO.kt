package org.softwaremaestro.domain.classroom.entity

data class TutoringInfoVO(
    val id: String,
    val requestId: String,
    val teacherId: String,
    val studentId: String,
    val status: String,
    val matchedAt: String,
    val endedAt: String,
    val whiteBoardAppId: String,
    val whiteBoardToken: String,
    val whiteBoardUUID: String,
)

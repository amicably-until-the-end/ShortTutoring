package org.softwaremaestro.domain.classroom.entity

data class TutoringInfoVO(
    val id: String,
    val teacherId: String,
    val studentId: String,
    val status: String? = null,
    val matchedAt: String? = null,
    val endedAt: String? = null,
    val whiteBoardAppId: String,
    val whiteBoardToken: String,
    val whiteBoardUUID: String,
    val RTCAppId: String,
    val studentRTCToken: String,
    val teacherRTCToken: String,
)

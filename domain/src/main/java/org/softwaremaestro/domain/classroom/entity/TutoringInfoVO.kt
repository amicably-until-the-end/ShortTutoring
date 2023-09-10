package org.softwaremaestro.domain.classroom.entity

data class TutoringInfoVO(
    val id: String? = null,
    val requestId: String? = null,
    val teacherId: String? = null,
    val studentId: String? = null,
    val status: String? = null,
    val matchedAt: String? = null,
    val endedAt: String? = null,
    val whiteBoardAppId: String? = null,
    val whiteBoardToken: String? = null,
    val whiteBoardUUID: String? = null,
)

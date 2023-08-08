package org.softwaremaestro.domain.question_check.entity

data class QuestionCheckResultVO(
    val studentSelect: String?,
    val status: String?,
    val tutoringId: String?,
    val whiteBoardToken: String?,
    val whiteBoardUUID: String?,
    val whiteBoardAppId: String?,
    val teacherRTCToken: String?,
    val studentRTCToken: String?,
    val RTCAppId: String?,
)
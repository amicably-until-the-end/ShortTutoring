package org.softwaremaestro.domain.question_upload.entity

data class TeacherPickResVO(
    val tutoringId: String,
    val whiteBoardToken: String,
    val whiteBoardUUID: String,
    val whiteBoardAppId: String,
    val teacherRTCToken: String,
    val studentRTCToken: String,
    val RTCAppId: String,
)

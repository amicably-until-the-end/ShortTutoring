package org.softwaremaestro.domain.question_upload.entity

data class TeacherPickReqVO(
    val questionId: String,
    val studentId: String,
    val teacherId: String,
)

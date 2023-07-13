package org.softwaremaestro.domain.question_check.entity

data class QuestionCheckResultVO(
    val requestId: String,
    val studentId: String,
    val status: String,
    val tutoringId: String
)
package org.softwaremaestro.domain.model.vo

data class QuestionVO (
    val photo: ByteArray,
    val subject: Int,
    val time: Int,
    val grade: Int,
    val reviews: List<Int>
)
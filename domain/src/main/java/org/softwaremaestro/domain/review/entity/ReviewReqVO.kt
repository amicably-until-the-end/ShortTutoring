package org.softwaremaestro.domain.review.entity

data class ReviewReqVO(
    val tutoringId: String,
    val rating: Int,
    val comment: String
)

package org.softwaremaestro.domain.review_get

data class ReviewVO(
    val name: String?,
    val createdAt: String?,
    val content: String?,
    val numOfThumbUp: Int?,
    val numOfThumbDown: Int?,
    val comments: List<String>?
)
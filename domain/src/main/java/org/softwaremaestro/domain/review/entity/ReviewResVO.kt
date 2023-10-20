package org.softwaremaestro.domain.review.entity

data class ReviewResVO(
    val questionId: String?,
    val tutoringId: String?,
    val reviewRating: Float?,
    val reviewComment: String?,
    val studentId: String?,
    val studentName: String?,
    val profileImage: String?,
    val role: String?,
    val schoolLevel: String?,
    val schoolGrade: Int?,
    val schoolName: String?,
)

package org.softwaremaestro.domain.question_get.entity

import java.time.LocalTime

data class QuestionGetResponseVO(
    var id: String?,
    val studentId: String?,
    val studentName: String?,
    val studentProfileImage: String?,
    val images: List<String>?,
    val mainImage: String?,
    val hopeTutoringTime: List<LocalTime>?,
    val hopeImmediately: Boolean?,
    val problemSchoolLevel: String?,
    val problemSubject: String?,
    val problemDescription: String?,
    val offerTeachers: List<String>?,
    val isSelect: Boolean?,
    val status: String?,
    val createdAt: String?,
    val tutoringId: String?,
    val chattingId: String?,
    val reservedStart: String?
)
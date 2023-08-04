package org.softwaremaestro.domain.question_get.entity;

data class QuestionGetResponseVO(
    var id: String?,
    val studentId: String?,
    val studentName: String?,
    val studentProfileImage: String?,
    val problemImage: String?,
    val problemSchoolLevel: String?,
    val problemSchoolSubject: String?,
    val problemDifficulty: String?,
    val problemDescription: String?,
    val teacherIds: List<String>?,
    val status: String?,
    val createdAt: String?,
    val tutoringId: String?
)
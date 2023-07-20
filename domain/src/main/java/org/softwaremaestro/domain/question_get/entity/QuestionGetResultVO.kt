package org.softwaremaestro.domain.question_get.entity;

data class QuestionGetResultVO (
    var id: String?,
    val studentId: String?,
    val problemBase64Image: String?,
    val problemSchoolLevel: String?,
    val problemSchoolChapter: String?,
    val problemSchoolSubject: String?,
    val problemDifficulty: String?,
    val problemDescription: String?,
    val status: String?,
    val tutoringId: String?,
    val createdAt: String?
)
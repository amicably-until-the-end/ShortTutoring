package org.softwaremaestro.domain.question_get.entity;

data class QuestionGetResultVO (
    val problemBase64Image: String?,
    val problemSchoolLevel: String?,
    val problemSchoolChapter: String?,
    val problemSchoolSubject: String?,
    val problemDifficulty: String?,
    val problemDescription: String?,
    val reviews: List<String>?,
    val teacherIds: List<String>?,
    val studentId: String?
)
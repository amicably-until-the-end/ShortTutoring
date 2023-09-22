package org.softwaremaestro.domain.question_selected_upload.entity

data class QuestionSelectedUploadResultVO(
    val id: String?,
    val student: StudentVO?,
    val teacherIds: List<String>?,
    val problem: ProblemVO?,
    val hopeTutorialTime: List<String>?,
    val hopeImmediately: Boolean?
)

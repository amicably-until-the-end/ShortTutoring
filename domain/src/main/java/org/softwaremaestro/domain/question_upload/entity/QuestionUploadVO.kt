package org.softwaremaestro.domain.question_upload.entity

data class QuestionUploadVO(
    var images: List<String>,
    var description: String,
    var schoolLevel: String,
    var schoolSubject: String,
)
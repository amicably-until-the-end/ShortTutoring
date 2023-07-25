package org.softwaremaestro.domain.question_upload.entity

data class QuestionUploadVO(
    var studentId: String,
    var imageBase64: String,
    var imageFormat: String,
    var description: String,
    var schoolLevel: String,
    var schoolSubject: String,
    var difficulty: String
)
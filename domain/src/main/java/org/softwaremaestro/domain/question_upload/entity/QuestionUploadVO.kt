package org.softwaremaestro.domain.question_upload.entity

data class QuestionUploadVO(
    var studentId: String,
    var description: String,
    var imageEncoding: String,
    var schoolLevel: String,
    var schoolSubject: String,
    var schoolChapter: String,
    var problemDifficulty: String,
)

package org.softwaremaestro.domain.question_upload.entity

import java.time.LocalDateTime

data class QuestionUploadVO(
    var images: List<String>,
    var description: String,
    var schoolLevel: String,
    var schoolSubject: String,
    var hopeImmediate: Boolean,
    var hopeTutoringTime: List<LocalDateTime>,
    var mainImageIndex: Int
)
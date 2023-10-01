package org.softwaremaestro.domain.question_selected_upload.entity

import java.time.LocalDateTime

data class QuestionSelectedUploadVO(
    var description: String,
    var schoolLevel: String,
    var schoolSubject: String,
    var mainImageIndex: Int,
    var images: List<String>,
    var requestTutoringStartTime: LocalDateTime,
    var requestTutoringEndTime: LocalDateTime,
    var requestTeacherId: String
)

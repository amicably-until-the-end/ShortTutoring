package org.softwaremaestro.domain.question_selected_upload.entity

data class QuestionSelectedUploadVO(
    var description: String,
    var schoolLevel: String,
    var schoolSubject: String,
    var mainImageIndex: Int,
    var images: List<String>,
    var requestTutoringStartTime: List<String>,
    var requestTutoringEndTime: List<String>,
    var requestTeacherId: String
)

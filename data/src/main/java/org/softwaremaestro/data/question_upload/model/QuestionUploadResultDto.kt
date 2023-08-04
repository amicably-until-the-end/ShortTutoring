package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName


data class QuestionUploadResultDto(
    @SerializedName("id") var questionId: String,
)

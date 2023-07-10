package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class QuestionUploadRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("detail") val detail: String,
)

package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class QuestionUploadRequestDto(
    @SerializedName("images") var images: List<String>,
    @SerializedName("description") var description: String,
    @SerializedName("schoolLevel") var schoolLevel: String,
    @SerializedName("schoolSubject") var schoolSubject: String,
)

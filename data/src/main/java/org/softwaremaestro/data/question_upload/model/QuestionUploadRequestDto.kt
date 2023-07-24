package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class QuestionUploadRequestDto(
    @SerializedName("imageBase64") var imageBase64: String,
    @SerializedName("imageFormat") var imageFormat: String,
    @SerializedName("description") var description: String,
    @SerializedName("schoolLevel") var schoolLevel: String,
    @SerializedName("schoolSubject") var schoolSubject: String,
    @SerializedName("difficulty") var difficulty: String
)

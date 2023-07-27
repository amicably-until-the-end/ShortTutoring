package org.softwaremaestro.data.question_check.model

import com.google.gson.annotations.SerializedName

data class QuestionCheckResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("tutoringId") val tutoringId: String,
    @SerializedName("whiteBoardToken") val whiteBoardToken: String,
    @SerializedName("whiteBoardUUID") val whiteBoardUUID: String,
    @SerializedName("whiteBoardAppId") val whiteBoardAppId: String
)
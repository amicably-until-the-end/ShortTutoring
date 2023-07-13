package org.softwaremaestro.data.question_check.model

import com.google.gson.annotations.SerializedName

data class QuestionCheckResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("tutoringId") val tutoringId: String
)
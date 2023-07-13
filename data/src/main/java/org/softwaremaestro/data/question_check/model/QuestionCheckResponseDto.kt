package org.softwaremaestro.data.question_check.model

import com.google.gson.annotations.SerializedName

data class QuestionCheckResponseDto(
    @SerializedName("id") val requestId: String,
    @SerializedName("student_id") val studentId: String,
    @SerializedName("status") val status: String,
    @SerializedName("tutoring_id") val tutoringId: String
)
package org.softwaremaestro.data.question_check.model

import com.google.gson.annotations.SerializedName

data class QuestionCheckRequestDto(
    @SerializedName("teacher_id") val teacherId: String
)
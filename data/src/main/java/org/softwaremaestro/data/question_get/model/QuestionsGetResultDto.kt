package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

data class QuestionsGetResultDto(
    @SerializedName("id") val id: String?,
    @SerializedName("student") val student: Student?,
    @SerializedName("problem") val problem: Problem?,
    @SerializedName("reviews") val reviews: List<String>?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("teacher_ids") val teacherIds: List<String>?
)

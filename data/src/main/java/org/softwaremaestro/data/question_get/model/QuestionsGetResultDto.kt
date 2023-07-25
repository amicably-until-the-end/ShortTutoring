package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

data class QuestionsGetResultDto(
    @SerializedName("id") val id: String?,
    @SerializedName("student") val student: StudentDto?,
    @SerializedName("problem") val problemDto: ProblemDto?,
    @SerializedName("teacherIds") val teacherIds: List<String>?,
    @SerializedName("createdAt") val createdAt: String?,
)

package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

data class QuestionsGetResultDto(
    @SerializedName("id") val id: String?,
    @SerializedName("student") val student: StudentDto?,
    @SerializedName("problem") val problemDto: ProblemDto?,
    @SerializedName("teacherIds") val teacherIds: List<String>?,
    @SerializedName("status") val status: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("tutoringId") val tutoringId: String?,
    @SerializedName("selectedTeacherId") val selectedTeacherId: String?,
    @SerializedName("hopeImmediately") val hopeImmediately: Boolean?,
    @SerializedName("hopeTutoringTime") val hopeTutoringTime: List<String>?
)

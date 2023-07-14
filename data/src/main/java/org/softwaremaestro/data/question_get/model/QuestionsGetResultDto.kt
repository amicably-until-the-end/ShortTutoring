package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

data class QuestionsGetResultDto(
    @SerializedName("id") val id: String?,
    @SerializedName("studentId") val studentId: String?,
    @SerializedName("problem") val problem: Problem?,
    @SerializedName("status") val status: String?,
    @SerializedName("tutoringId") val tutoringId: String?,
    @SerializedName("created_at") val createdAt: String?,
)

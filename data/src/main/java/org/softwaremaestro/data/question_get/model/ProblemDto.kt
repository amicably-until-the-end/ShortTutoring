package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

data class ProblemDto(
    @SerializedName("schoolLevel") val schoolLevel: String?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("schoolSubject") val schoolSubject: String?,
    @SerializedName("image") val image: String?
)
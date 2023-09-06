package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

data class ProblemDto(
    @SerializedName("schoolLevel") val schoolLevel: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("mainImage") val mainImage: String?,
    @SerializedName("schoolSubject") val schoolSubject: String?,
    @SerializedName("images") val image: List<String>?
)
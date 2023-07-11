package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

data class Problem(
    @SerializedName("school_level") val schoolLevel: String?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("school_subject") val schoolSubject: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("school_chapter") val schoolChapter: String?
)
package org.softwaremaestro.data.follow.model

import com.google.gson.annotations.SerializedName

data class SchoolStudentDto(
    @SerializedName("level") val level: String?,
    @SerializedName("grade") val grade: Int?
)

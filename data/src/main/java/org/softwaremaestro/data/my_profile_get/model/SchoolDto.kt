package org.softwaremaestro.data.my_profile_get.model

import com.google.gson.annotations.SerializedName

data class SchoolDto(
    @SerializedName("level") val level: String?,
    @SerializedName("grade") val grade: Int?
)
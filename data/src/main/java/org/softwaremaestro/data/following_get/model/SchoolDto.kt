package org.softwaremaestro.data.following_get.model

import com.google.gson.annotations.SerializedName

data class SchoolDto(
    @SerializedName("division") val division: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("department") val department: String?,
    @SerializedName("grade") val grade: Int?
)

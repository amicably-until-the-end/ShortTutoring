package org.softwaremaestro.data.profile.model

import com.google.gson.annotations.SerializedName

data class SchoolDto(
    @SerializedName("level") val level: String?,
    @SerializedName("grade") val grade: Int?,
    @SerializedName("name") val schoolName: String?,
    @SerializedName("department") val schoolDepartment: String?
)
package org.softwaremaestro.data.follow.model

import com.google.gson.annotations.SerializedName

data class SchoolTeacherDto(
    @SerializedName("division") val division: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("department") val department: String?,
    @SerializedName("grade") val grade: Int?
)

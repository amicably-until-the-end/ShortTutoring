package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class TeacherRegisterReqDto(
    @SerializedName("univ") val school: String?,
    @SerializedName("colledge") val grade: String?,
    @SerializedName("major") val major: String?,
    @SerializedName("admissonYear") val admissonYear: String?,
)

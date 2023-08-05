package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class TeacherRegisterReqDto(
    @SerializedName("bio") val bio: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("vendor") val vendor: String
)

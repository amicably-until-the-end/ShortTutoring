package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class StudentRegisterReqDto(
    @SerializedName("bio") val bio: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("authorizationCode") val authCode: String,
)

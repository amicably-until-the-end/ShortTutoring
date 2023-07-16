package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class UserInfoResDto(
    @SerializedName("role") val role: String,
    @SerializedName("name") val name: String,
)

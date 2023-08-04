package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class LoginResDto(
    @SerializedName("token") val JWT: String,
    @SerializedName("role") val role: String,
)

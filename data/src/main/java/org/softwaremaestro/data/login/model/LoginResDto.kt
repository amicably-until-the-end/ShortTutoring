package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class LoginResDto(
    @SerializedName("role") val vendor: String,
    @SerializedName("JWT") val accessCode: String,
)

package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class LoginReqDto(
    @SerializedName("vendor") val vendor: String,
    @SerializedName("accessToken") val accessToken: String
)

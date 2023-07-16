package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class UserInfoReqDto(
    @SerializedName("userToken") val userToken: String
)

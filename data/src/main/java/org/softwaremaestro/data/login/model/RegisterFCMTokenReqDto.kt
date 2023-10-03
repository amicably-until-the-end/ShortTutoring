package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class RegisterFCMTokenReqDto(
    @SerializedName("fcmToken") val fcmToken: String,
)

package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class StudentRegisterReqDto(
    @SerializedName("school") val school: Int?,
    @SerializedName("grade") val grade: Int?,
)

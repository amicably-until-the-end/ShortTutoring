package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class StudentRegisterReqDto(
    @SerializedName("bio") val bio: String,
    @SerializedName("name") val name: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("vendor") val vendor: String,
    @SerializedName("schoolLevel") val schoolLevel: String,
    @SerializedName("schoolGrade") val schoolGrade: Int,
)

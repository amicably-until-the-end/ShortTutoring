package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class TeacherRegisterReqDto(
    @SerializedName("vendor") val vendor: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("name") val name: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("profileImg") val profileImg: String,
    @SerializedName("schoolName") val schoolName: String,
    @SerializedName("schoolDepartment") val schoolDepartment: String
)

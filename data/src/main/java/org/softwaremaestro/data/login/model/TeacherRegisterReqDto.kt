package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class TeacherRegisterReqDto(
    @SerializedName("bio") val bio: String,
    @SerializedName("name") val name: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("vendor") val vendor: String,
    @SerializedName("schoolName") val schoolName: String,
    @SerializedName("schoolDivision") val schoolDivision: String,
    @SerializedName("schoolDepartment") val schoolDepartment: String,
    @SerializedName("schoolGrade") val schoolGrade: Int,
)

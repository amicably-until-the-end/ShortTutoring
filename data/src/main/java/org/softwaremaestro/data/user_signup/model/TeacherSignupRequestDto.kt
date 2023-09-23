package org.softwaremaestro.data.user_signup.model

import com.google.gson.annotations.SerializedName

data class TeacherSignupRequestDto(
    @SerializedName("vendor") val vendor: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("name") val name: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("schoolName") val schoolName: String,
    @SerializedName("schoolDivision") val schoolDivision: String,
    @SerializedName("schoolDepartment") val schoolDepartment: String,
    @SerializedName("schoolGrade") val schoolGrade: Long
)
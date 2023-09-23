package org.softwaremaestro.data.user_signup.model

import com.google.gson.annotations.SerializedName

data class StudentSignupRequestDto(
    @SerializedName("vendor") val vendor: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("name") val name: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("schoolLevel") val schoolLevel: String,
    @SerializedName("schoolGrade") val schoolGrade: Long
)
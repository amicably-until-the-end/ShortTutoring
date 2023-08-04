package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class UserInfoResDto(
    @SerializedName("role") val role: String,
    @SerializedName("id") val id: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("name") val name: String,
    @SerializedName("profileImage") val profileImage: String,
)

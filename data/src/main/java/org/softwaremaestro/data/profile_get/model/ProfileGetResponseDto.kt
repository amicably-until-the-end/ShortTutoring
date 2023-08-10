package org.softwaremaestro.data.profile_get.model

import com.google.gson.annotations.SerializedName

data class ProfileGetResponseDto(
    @SerializedName("following") val following: List<String>?,
    @SerializedName("school") val school: SchoolDto?,
    @SerializedName("role") val role: String?,
    @SerializedName("followers") val followers: List<String>?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profileImage") val profileImage: String?
)
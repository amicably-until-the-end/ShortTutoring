package org.softwaremaestro.data.profile.model

import com.google.gson.annotations.SerializedName

data class ProfileUpdateResponseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("profileImageBase64") val profileImageBase64: String?,
    @SerializedName("profileImageFormat") val profileImageFormat: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("school") val school: SchoolDto?,
    @SerializedName("followersCount") val followersCount: Int?,
    @SerializedName("followingCount") val followingCount: Int?
)

package org.softwaremaestro.data.follow.model

import com.google.gson.annotations.SerializedName

data class FollowerGetResponseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profileImage") val profileImage: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("schoolLevel") val schoolLevel: String?,
    @SerializedName("grade") val grade: Int?
)
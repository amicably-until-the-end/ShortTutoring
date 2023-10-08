package org.softwaremaestro.data.follow.model

import com.google.gson.annotations.SerializedName

data class FollowingGetResponseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profileImage") val profileImage: String?,
    @SerializedName("followerIds") val followers: List<String>?,
    @SerializedName("reserveCnt") val reserveCnt: Int?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("major") val major: String?,
    @SerializedName("univ") val univ: String?,
)

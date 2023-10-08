package org.softwaremaestro.data.profile.model

import com.google.gson.annotations.SerializedName

data class MyProfileResDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("coin") val coin: CoinDto?,
    @SerializedName("profileImage") val profileImage: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("school") val school: SchoolDto?,
    @SerializedName("followers") val followers: List<String>?,
    @SerializedName("following") val following: List<String>?,
    @SerializedName("participatingChattingRooms") val participatingChattingRooms: List<String>?
)
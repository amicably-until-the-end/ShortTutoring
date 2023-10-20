package org.softwaremaestro.data.best_teacher_get.model

import com.google.gson.annotations.SerializedName
import org.softwaremaestro.data.profile.model.CoinDto
import org.softwaremaestro.data.profile.model.SchoolDto

data class BestTeacherGetResDto(
    @SerializedName("following") val following: List<String>?,
    @SerializedName("school") val school: SchoolDto?,
    @SerializedName("participatingChattingRooms") val participatingChattingRooms: List<String>?,
    @SerializedName("followers") val followers: List<String>?,
    @SerializedName("role") val role: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("coin") val coin: CoinDto,
    @SerializedName("profileImage") val profileImage: String?,
    @SerializedName("rating") val rating: Float?
)

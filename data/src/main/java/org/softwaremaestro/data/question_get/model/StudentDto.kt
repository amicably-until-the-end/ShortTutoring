package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName
import org.softwaremaestro.data.profile.model.SchoolDto

data class StudentDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profileImage") val profileImage: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("followers") val followers: List<String>?,
    @SerializedName("followingCount") val followingCount: Int?,
    @SerializedName("school") val school: SchoolDto?
)









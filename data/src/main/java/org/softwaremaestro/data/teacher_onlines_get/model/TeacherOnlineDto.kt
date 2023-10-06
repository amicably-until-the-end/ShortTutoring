package org.softwaremaestro.data.teacher_onlines_get.model

import com.google.gson.annotations.SerializedName

data class TeacherOnlineDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profileImage") val profileImage: String?,
    @SerializedName("followers") val followers: Int?
)

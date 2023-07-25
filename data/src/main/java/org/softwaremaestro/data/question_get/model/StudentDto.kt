package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

data class StudentDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profileImage") val profileImage: String?
)









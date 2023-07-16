package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class TeacherDto(
    @SerializedName("id") val id: String,
    @SerializedName("role") val name: String,
    @SerializedName("name") val bio: String,
    @SerializedName("profileImageURL") val imageUrl: String?,
)

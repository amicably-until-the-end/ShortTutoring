package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class TeacherDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("profileImageURL") val imageUrl: String,
)

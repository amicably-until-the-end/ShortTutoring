package org.softwaremaestro.data.question_get.model

import com.google.gson.annotations.SerializedName

class Student(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("role") val role: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("profileImageURL") val profileImageURL: String
)
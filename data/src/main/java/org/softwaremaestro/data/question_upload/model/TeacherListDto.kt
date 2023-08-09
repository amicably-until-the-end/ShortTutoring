package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class TeacherListDto(
    @SerializedName("teachers") val teacherList: List<TeacherInfo>
)

data class TeacherInfo(
    @SerializedName("id") val id: String,
    @SerializedName("role") val role: String,
    @SerializedName("name") val name: String,
)

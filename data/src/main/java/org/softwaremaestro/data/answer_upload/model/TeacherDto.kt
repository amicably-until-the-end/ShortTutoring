package org.softwaremaestro.data.answer_upload.model

import com.google.gson.annotations.SerializedName

data class TeacherDto(
    @SerializedName("teacherId") val teacherId: String
)

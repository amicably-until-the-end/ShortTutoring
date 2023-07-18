package org.softwaremaestro.data.answer_upload.model

import com.google.gson.annotations.SerializedName

data class AnswerUploadRequestDto(
    @SerializedName("requestId") val id: String,
    val teacherDto: TeacherDto
)
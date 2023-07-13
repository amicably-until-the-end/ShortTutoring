package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class PickTeacherDto(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("teacherId") val teacherId: String,
    @SerializedName("requestId") val questionId: String
)

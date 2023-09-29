package org.softwaremaestro.data.classroom.model

import com.google.gson.annotations.SerializedName


data class TutoringInfoResDto(
    @SerializedName("tutoring") val tutoring: TutoringInfoDto
)

data class TutoringInfoDto(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("status") val status: String,
    @SerializedName("teacherId") val teacherId: String,
    @SerializedName("reservedStart") val reservedStart: String,
    @SerializedName("reservedEnd") val reservedEnd: String,
)

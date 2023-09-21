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
    @SerializedName("endedAt") val endedAt: String,
    @SerializedName("RTCAppId") val RTCAppId: String,
    @SerializedName("startedAt") val startedAt: String,
    @SerializedName("whiteBoardUUID") val whiteBoardUUID: String,
    @SerializedName("whiteBoardToken") val whiteBoardToken: String,
    @SerializedName("whiteBoardAppId") val whiteBoardAppId: String,
    @SerializedName("matchedAt") val matchedAt: String,
    @SerializedName("teacherRTCToken") val teacherRTCToken: String,
    @SerializedName("studentRTCToken") val studentRTCToken: String,

    )

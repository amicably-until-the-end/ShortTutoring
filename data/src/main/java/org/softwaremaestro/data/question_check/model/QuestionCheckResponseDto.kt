package org.softwaremaestro.data.question_check.model

import com.google.gson.annotations.SerializedName

data class QuestionCheckResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("tutoring") val tutoring: Tutoring,
)

data class Tutoring(
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


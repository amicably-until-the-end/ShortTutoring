package org.softwaremaestro.data.classroom.model

import com.google.gson.annotations.SerializedName

data class TutoringInfoDto(
    @SerializedName("id") val tutoringId: String,
    @SerializedName("requestId") val requestId: String,
    @SerializedName("teacherId") val teacherId: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("status") val status: String,
    @SerializedName("matchedAt") val matchedAt: String,
    @SerializedName("endedAt") val endedAt: String,
    @SerializedName("whiteBoardAppId") val whiteBoardAppId: String,
    @SerializedName("whiteBoardToken") val whiteBoardToken: String,
    @SerializedName("whiteBoardUUID") val whiteBoardUUID: String,
)

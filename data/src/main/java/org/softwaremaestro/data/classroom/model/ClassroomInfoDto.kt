package org.softwaremaestro.data.classroom.model

import com.google.gson.annotations.SerializedName


data class ClassroomInfoDto(
    @SerializedName("boardAppId") val id: String,
    @SerializedName("boardUUID") val studentId: String,
    @SerializedName("boardToken") val status: String,
    @SerializedName("rtcToken") val rtcToken: String,
    @SerializedName("rtcAppId") val rtcAppId: String,
    @SerializedName("rtcChannel") val rtcChannel: String,
    @SerializedName("rtcUID") val rtcUID: Int,
)

package org.softwaremaestro.domain.classroom.entity

import java.io.Serializable

data class ClassroomInfoVO(
    val boardAppId: String,
    val boardUUID: String,
    val rtcToken: String,
    val boardToken: String,
    val rtcChannel: String,
    val rtcAppId: String,
    val rtcUID: Int,
) : Serializable {
    companion object {
        const val NOT_YET_START = "NOT_YET_START"
    }
}


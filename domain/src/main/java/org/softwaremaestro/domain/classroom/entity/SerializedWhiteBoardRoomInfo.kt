package org.softwaremaestro.domain.classroom.entity

import java.io.Serializable

data class SerializedWhiteBoardRoomInfo(
    val appId: String,
    val uuid: String,
    val roomToken: String,
    val uid: String
) : Serializable


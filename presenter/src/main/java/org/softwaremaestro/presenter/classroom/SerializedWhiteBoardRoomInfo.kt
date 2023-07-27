package org.softwaremaestro.presenter.classroom

import java.io.Serializable

data class SerializedWhiteBoardRoomInfo(
    val appId: String,
    val uuid: String,
    val roomToken: String,
    val uid: String
) : Serializable


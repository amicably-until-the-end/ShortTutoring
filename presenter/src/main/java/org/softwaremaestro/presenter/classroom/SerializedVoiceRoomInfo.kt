package org.softwaremaestro.presenter.classroom

import java.io.Serializable

data class SerializedVoiceRoomInfo(
    val appId: String,
    val uuid: String,
    val roomToken: String,
    val uid: String
) : Serializable


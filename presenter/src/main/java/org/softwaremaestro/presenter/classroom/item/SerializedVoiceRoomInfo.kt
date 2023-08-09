package org.softwaremaestro.presenter.classroom.item

import java.io.Serializable

data class SerializedVoiceRoomInfo(
    val appId: String,
    val token: String,
    val channelId: String,
    val uid: Int,
) : Serializable


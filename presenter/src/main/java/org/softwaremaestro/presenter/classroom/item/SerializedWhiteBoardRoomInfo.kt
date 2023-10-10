package org.softwaremaestro.presenter.classroom.item

import java.io.Serializable

data class SerializedWhiteBoardRoomInfo(
    val appId: String,
    val uuid: String,
    val roomToken: String,
    val uid: String,
    val questionId: String,
    val roomTitle: String,
    val roomProfileImage: String,
    val opponentName: String,
    val isTeacher: Boolean,
    val tutoringId: String
) : Serializable


package org.softwaremaestro.data.answer_upload.model

import com.google.gson.annotations.SerializedName

data class AnswerUploadResDto(
    @SerializedName("chatRoomId") val chatRoomId: String?
)
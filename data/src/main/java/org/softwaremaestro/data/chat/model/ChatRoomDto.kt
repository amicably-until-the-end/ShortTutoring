package org.softwaremaestro.data.chat.model

import com.google.gson.annotations.SerializedName

data class ChatRoomListDto(
    @SerializedName("normalProposed") val normalProposed: List<ChatRoomDto>,
    @SerializedName("normalReserved") val normalReserved: List<ChatRoomDto>,
    @SerializedName("selectedProposed") val selectedProposed: List<ChatRoomDto>,
    @SerializedName("selectedReserved") val selectedReserved: List<ChatRoomDto>,
)


data class ChatRoomDto(
    @SerializedName("id") val id: String?,
    @SerializedName("roomImage") val roomImage: String,
    @SerializedName("questionState") val questionState: String?,
    @SerializedName("opponentId") val opponentId: String?,
    @SerializedName("title") val title: String,
    @SerializedName("isSelect") val isSelect: Boolean?,
    @SerializedName("questionId") val questionId: String?,
    @SerializedName("schoolSubject") val schoolSubject: String?,
    @SerializedName("schoolLevel") val schoolLevel: String?,
    @SerializedName("messages") val messages: List<MessageDto>?,
    @SerializedName("teachers") val teachers: List<ChatRoomDto>?,
    @SerializedName("isTeacherRoom") val isTeacherRoom: Boolean?,
)

data class MessageDto(
    @SerializedName("sender") val sender: String,
    @SerializedName("format") val format: String,
    @SerializedName("body") val body: MessageBodyDto,
    @SerializedName("createdAt") val time: String,
    @SerializedName("isMyMsg") val isMyMsg: Boolean,
)


data class MessageBodyDto(
    @SerializedName("text") val text: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("startDateTime") val startDateTime: String?,
    @SerializedName("questionId") val questionId: String?,

    )


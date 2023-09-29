package org.softwaremaestro.data.chat.model

import com.google.gson.annotations.SerializedName
import org.softwaremaestro.data.question_get.model.ProblemDto
import org.softwaremaestro.data.question_get.model.QuestionsGetResultDto

data class ChatRoomListDto(
    @SerializedName("normalProposed") val normalProposed: List<ChatRoomDto>,
    @SerializedName("normalReserved") val normalReserved: List<ChatRoomDto>,
    @SerializedName("selectedProposed") val selectedProposed: List<ChatRoomDto>,
    @SerializedName("selectedReserved") val selectedReserved: List<ChatRoomDto>,
)


data class ChatRoomDto(
    @SerializedName("id") val id: String?,
    @SerializedName("roomImage") val roomImage: String,
    @SerializedName("status") val questionState: String?,
    @SerializedName("opponentId") val opponentId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("isSelect") val isSelect: Boolean?,
    @SerializedName("questionId") val questionId: String?,
    @SerializedName("messages") val messages: List<MessageDto>?,
    @SerializedName("questionInfo") val questionInfo: QuestionInfoDto?,
)

data class QuestionInfoDto(
    @SerializedName("id") val id: String,
    @SerializedName("problem") val problem: ProblemDto,
)

data class MessageDto(
    @SerializedName("sender") val sender: String,
    @SerializedName("format") val format: String,
    //@SerializedName("body") val body: MessageBodyDto,
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


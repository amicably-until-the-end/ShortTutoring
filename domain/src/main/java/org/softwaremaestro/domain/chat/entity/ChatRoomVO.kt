package org.softwaremaestro.domain.chat.entity

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class ChatRoomListVO(
    val normalProposed: List<ChatRoomVO>,
    val normalReserved: List<ChatRoomVO>,
    val selectedProposed: List<ChatRoomVO>,
    val selectedReserved: List<ChatRoomVO>,
    var currentRoomVO: ChatRoomVO? = null,
)


data class ChatRoomVO(
    val id: String? = null,
    val roomType: RoomType,
    val roomImage: String?,
    val questionState: QuestionState,
    val questionId: String?,
    val opponentId: String? = null,
    val title: String?,
    val schoolSubject: String? = null,
    val startDateTime: LocalDateTime? = null,
    val description: String,
    val schoolLevel: String? = null,
    val messages: List<MessageVO>? = null,
    var teachers: List<ChatRoomVO>? = null,
    val isSelect: Boolean,
    val subTitle: String? = null,
)

data class StudentInfoVO(
    val id: String,
    val name: String,
    val profileImageUrl: String,
)

data class TeacherInfoVO(
    val id: String,
    val name: String,
    val profileImageUrl: String,
)

data class MessageVO(
    val time: LocalDateTime,
    val bodyVO: MessageBodyVO?,
    val sender: String? = null,
    val isMyMsg: Boolean,
)


sealed class MessageBodyVO {
    @Serializable
    data class ProblemImage(
        val image: String?,
        val description: String?,
        val questionId: String?,
        val subTitle: String?,
    ) : MessageBodyVO()

    @Serializable
    data class AppointRequest(
        val startDateTime: String?,
    ) : MessageBodyVO()

    @Serializable
    data class Text(
        val text: String?,
    ) : MessageBodyVO()

    @Serializable
    object RequestDecline : MessageBodyVO()

    @Serializable
    data class ReserveConfirm(
        val startDateTime: String?,
    ) : MessageBodyVO()
}

enum class QuestionType {
    NORMAL, SELECTED
}

enum class QuestionState {
    PROPOSED, RESERVED
}

enum class RoomType {
    QUESTION, TEACHER
}
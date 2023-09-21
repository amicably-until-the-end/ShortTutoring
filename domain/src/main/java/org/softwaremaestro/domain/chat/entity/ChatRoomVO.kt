package org.softwaremaestro.domain.chat.entity

import java.awt.TrayIcon
import java.time.LocalDate
import java.time.LocalDateTime

data class ChatRoomListVO(
    val normalProposed: List<ChatRoomVO>,
    val normalReserved: List<ChatRoomVO>,
    val selectedProposed: List<ChatRoomVO>,
    val selectedReserved: List<ChatRoomVO>,
)

sealed class NormalProposedRoomVO {
    data class QuestionRoomVO(val value: NormalQuestionRoomVO) : NormalProposedRoomVO()
    data class TeacherRoomVO(val value: ChatRoomVO) : NormalProposedRoomVO()
}


data class ChatRoomVO(
    val id: String?,
    val roomType: RoomType,
    val roomImage: String,
    val questionState: QuestionState,
    val questionId: String?,
    val opponentId: String?,
    val title: String,
    val schoolSubject: String?,
    val schoolLevel: String?,
    val messages: List<MessageVO>?,
    val teachers: List<ChatRoomVO>?,
    val isSelect: Boolean,
)

data class NormalQuestionRoomVO(
    val teachers: List<ChatRoomVO>,
    val students: List<ChatRoomVO>,
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
    val sender: String?,
    val isMyMsg: Boolean,
)

sealed class MessageBodyVO {
    data class ProblemImage(
        val imageUrl: String?,
        val description: String?,
    ) : MessageBodyVO()

    data class AppointRequest(
        val startDateTime: LocalDateTime?,
    ) : MessageBodyVO()

    data class Text(
        val text: String?,
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
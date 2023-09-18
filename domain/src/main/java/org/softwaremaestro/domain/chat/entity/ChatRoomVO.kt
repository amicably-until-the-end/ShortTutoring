package org.softwaremaestro.domain.chat.entity


data class ChatRoomVO(
    val id: String,
    val roomType: Int,
    val studentInfo: StudentInfoVO,
    val teacherInfo: TeacherInfoVO,
    val questionInfo: QuestionInfoVO,
    val newMessage: Int = 0,
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

data class QuestionInfoVO(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val category: String,
    val createdAt: String,
    val updatedAt: String,
    val isAnswered: Boolean,
    val isDeleted: Boolean,
)

enum class QuestionType {
    NORMAL, SELECTED
}

enum class QuestionState {
    PROPOSED, RESERVED
}

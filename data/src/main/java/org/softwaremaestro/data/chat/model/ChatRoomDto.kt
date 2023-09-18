package org.softwaremaestro.data.chat.model

import com.google.gson.annotations.SerializedName

data class ChatRoomListDto(
    @SerializedName("normalQuestion") val normalQuestionList: List<ChatRoomDto>,
    @SerializedName("reservedQuestion") val reservedQuestionList: List<ChatRoomDto>
)

data class ChatRoomDto(
    @SerializedName("id") val tutoringId: String,
    @SerializedName("studentInfo") val studentInfo: StudentInfoDto,
    @SerializedName("roomType") val roomType: Int,
    @SerializedName("teacherInfo") val teacherInfo: TeacherInfoDto,
    @SerializedName("questionInfo") val questionInfo: QuestionInfoDto
)

data class StudentInfoDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String,
)

data class TeacherInfoDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String,
)

data class QuestionInfoDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("category") val category: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("isAnswered") val isAnswered: Boolean,
    @SerializedName("isDeleted") val isDeleted: Boolean,
)

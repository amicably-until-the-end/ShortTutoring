package org.softwaremaestro.data.question.dto

import com.google.gson.annotations.SerializedName

// Todo: Request 테이블 구성 완료되면 수정
data class QuestionDto(
    @SerializedName("student_id")
    val studentId: Int,
    val studentName: String
)

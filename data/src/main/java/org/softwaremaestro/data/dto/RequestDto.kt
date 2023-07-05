package org.softwaremaestro.data.dto

import com.google.gson.annotations.SerializedName

// Todo: Request 테이블 구성 완료되면 수정
data class RequestDto(
    @SerializedName("student_id")
    val studentId: Int
)

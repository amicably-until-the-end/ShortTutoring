package org.softwaremaestro.data.response

import com.google.gson.annotations.SerializedName

// Todo: 추후 테이블 구성 완료되면 수정
data class ResponseDto(
    @SerializedName("teacher_id")
    val teacherId: Int
)

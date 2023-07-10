package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

// Todo: Request 테이블 구성 완료되면 수정
data class QuestionUploadResultDto(
    @SerializedName("question_id") var questionId: Int,
)

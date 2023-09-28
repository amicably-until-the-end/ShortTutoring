package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PickTeacherReqDto(
    @SerializedName("chattingId") val chattingId: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("startTime") val startTime: String
)

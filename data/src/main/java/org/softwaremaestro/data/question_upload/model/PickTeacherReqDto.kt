package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class PickTeacherReqDto(
    @SerializedName("chattingId") val chattingId: String,
)

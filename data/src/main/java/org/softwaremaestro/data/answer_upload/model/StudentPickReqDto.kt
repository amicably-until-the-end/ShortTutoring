package org.softwaremaestro.data.answer_upload.model

import com.google.gson.annotations.SerializedName

data class StudentPickReqDto(
    @SerializedName("chattingId") val chattingId: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,

    )

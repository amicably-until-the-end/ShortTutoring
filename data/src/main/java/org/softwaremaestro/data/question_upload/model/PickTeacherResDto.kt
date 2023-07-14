package org.softwaremaestro.data.question_upload.model

import com.google.gson.annotations.SerializedName

data class PickTeacherResDto(
    @SerializedName("tutoringId") val tutoringId: String,
)

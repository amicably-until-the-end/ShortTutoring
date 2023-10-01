package org.softwaremaestro.data.question_selected_upload.model

import com.google.gson.annotations.SerializedName

data class QuestionSelectedUploadReqDto(
    @SerializedName("description") val description: String?,
    @SerializedName("schoolLevel") val schoolLevel: String?,
    @SerializedName("schoolSubject") val schoolSubject: String?,
    @SerializedName("mainImageIndex") val mainImageIndex: Int?,
    @SerializedName("images") val images: List<String>?,
    @SerializedName("requestTutoringStartTime") val requestTutoringStartTime: String,
    @SerializedName("requestTutoringEndTime") val requestTutoringEndTime: String,
    @SerializedName("requestTeacherId") val requestTeacherId: String?
)

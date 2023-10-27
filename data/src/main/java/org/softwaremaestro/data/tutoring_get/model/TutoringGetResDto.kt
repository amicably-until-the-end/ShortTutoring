package org.softwaremaestro.data.tutoring_get.model

import com.google.gson.annotations.SerializedName

data class TutoringGetResDto(
    @SerializedName("tutoringId") val tutoringId: String?,
    @SerializedName("questionId") val questionId: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("schoolLevel") val schoolLevel: String?,
    @SerializedName("schoolSubject") val schoolSubject: String?,
    @SerializedName("tutoringDate") val tutoringDate: String?,
    @SerializedName("opponentName") val opponentName: String?,
    @SerializedName("opponentProfileImage") val opponentProfileImage: String?,
    @SerializedName("recordFileUrl") val recordFileUrl: List<String>?,
    @SerializedName("questionImage") val questionImage: String?,
    @SerializedName("opponentId") val opponentId: String?
)
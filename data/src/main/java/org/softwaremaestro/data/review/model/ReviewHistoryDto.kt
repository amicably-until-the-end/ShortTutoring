package org.softwaremaestro.data.review.model

import com.google.gson.annotations.SerializedName
import org.softwaremaestro.data.question_get.model.StudentDto

data class ReviewHistoryDto(
    @SerializedName("questionId") val questionId: String?,
    @SerializedName("studentId") val studentId: String?,
    @SerializedName("tutoringId") val tutoringId: String?,
    @SerializedName("reviewRating") val reviewRating: Float?,
    @SerializedName("reviewComment") val reviewComment: String?,
    @SerializedName("startedAt") val startedAt: String?,
    @SerializedName("endedAt") val endedAt: String?,
    @SerializedName("student") val student: StudentDto?,
)

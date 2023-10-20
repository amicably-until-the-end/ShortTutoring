package org.softwaremaestro.data.review.model

import com.google.gson.annotations.SerializedName

data class ReviewGetResDto(
    @SerializedName("count") val count: Int?,
    @SerializedName("history") val history: List<ReviewHistoryDto>?
)

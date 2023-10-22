package org.softwaremaestro.data.review.model

import com.google.gson.annotations.SerializedName

data class ReviewCreateReqDto(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String
)

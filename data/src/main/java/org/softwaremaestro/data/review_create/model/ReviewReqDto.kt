package org.softwaremaestro.data.review_create.model

import com.google.gson.annotations.SerializedName

data class ReviewReqDto(
    @SerializedName("rating") val rating: Float,
    @SerializedName("comment") val comment: String
)

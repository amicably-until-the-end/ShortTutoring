package org.softwaremaestro.data.review_create.model

import com.google.gson.annotations.SerializedName

data class ReviewReqDto(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String
)

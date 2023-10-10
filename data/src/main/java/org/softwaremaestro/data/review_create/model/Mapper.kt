package org.softwaremaestro.data.review_create.model

import org.softwaremaestro.domain.review_create.entity.ReviewVO

object Mapper {
    fun asDto(reviewCreateVO: ReviewVO): ReviewReqDto {
        return ReviewReqDto(
            rating = reviewCreateVO.rating,
            comment = reviewCreateVO.comment
        )
    }
}


fun ReviewVO.asDto(): ReviewReqDto {
    return Mapper.asDto(this)
}

package org.softwaremaestro.domain.review_create

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.review_create.entity.ReviewVO


interface ReviewCreateRepository {
    suspend fun createReview(reviewReqVO: ReviewVO): Flow<BaseResult<String, String>>
}
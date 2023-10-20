package org.softwaremaestro.domain.review

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.review.entity.ReviewReqVO
import org.softwaremaestro.domain.review.entity.ReviewResVO


interface ReviewRepository {
    suspend fun getReview(teacherId: String): Flow<BaseResult<List<ReviewResVO>, String>>
    suspend fun createReview(reviewReqVO: ReviewReqVO): Flow<BaseResult<String, String>>
}
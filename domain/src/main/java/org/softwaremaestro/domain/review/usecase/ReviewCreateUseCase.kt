package org.softwaremaestro.domain.review.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.review.ReviewRepository
import org.softwaremaestro.domain.review.entity.ReviewReqVO
import javax.inject.Inject

class ReviewCreateUseCase @Inject constructor(private val repository: ReviewRepository) {

    suspend fun execute(reviewReqVO: ReviewReqVO): Flow<BaseResult<String, String>> =
        repository.createReview(reviewReqVO)
}
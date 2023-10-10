package org.softwaremaestro.domain.review_create.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.review_create.ReviewCreateRepository
import org.softwaremaestro.domain.review_create.entity.ReviewVO
import javax.inject.Inject

class ReviewCreateUseCase @Inject constructor(private val repository: ReviewCreateRepository) {

    suspend fun execute(reviewVO: ReviewVO): Flow<BaseResult<String, String>> =
        repository.createReview(reviewVO)
}
package org.softwaremaestro.domain.review.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.review.ReviewRepository
import org.softwaremaestro.domain.review.entity.ReviewResVO
import javax.inject.Inject

class ReviewGetUseCase @Inject constructor(private val repository: ReviewRepository) {

    suspend fun execute(teacherId: String): Flow<BaseResult<List<ReviewResVO>, String>> =
        repository.getReview(teacherId)
}
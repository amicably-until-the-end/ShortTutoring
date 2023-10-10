package org.softwaremaestro.data.review_create

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.review_create.model.asDto
import org.softwaremaestro.data.review_create.remote.ReviewCreateApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.review_create.ReviewCreateRepository
import org.softwaremaestro.domain.review_create.entity.ReviewVO
import javax.inject.Inject

class ReviewCreateRepositoryImpl @Inject constructor(private val reviewCreateApi: ReviewCreateApi) :
    ReviewCreateRepository {

    override suspend fun createReview(reviewVO: ReviewVO): Flow<BaseResult<String, String>> {
        return flow {
            val dto = reviewVO.asDto()
            val response = reviewCreateApi.createReview(reviewVO.tutoringId, dto)
            if (response.isSuccessful) {
                emit(BaseResult.Success("review create success"))
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
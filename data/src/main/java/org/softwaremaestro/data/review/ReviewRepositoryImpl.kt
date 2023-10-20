package org.softwaremaestro.data.review

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.review.model.asDomain
import org.softwaremaestro.data.review.model.asDto
import org.softwaremaestro.data.review.remote.ReviewApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.review.ReviewRepository
import org.softwaremaestro.domain.review.entity.ReviewReqVO
import org.softwaremaestro.domain.review.entity.ReviewResVO
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(private val reviewApi: ReviewApi) :
    ReviewRepository {

    override suspend fun getReview(teacherId: String): Flow<BaseResult<List<ReviewResVO>, String>> {
        return flow {
            val response = reviewApi.getReview(teacherId)
            if (response.isSuccessful) {
                val body = response.body() ?: return@flow
                val data = body.data?.asDomain() ?: return@flow
                emit(BaseResult.Success(data))
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }

    override suspend fun createReview(reviewReqVO: ReviewReqVO): Flow<BaseResult<String, String>> {
        return flow {
            val dto = reviewReqVO.asDto()
            val response = reviewApi.createReview(reviewReqVO.tutoringId, dto)
            if (response.isSuccessful) {
                emit(BaseResult.Success("review create success"))
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
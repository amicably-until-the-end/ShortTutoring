package org.softwaremaestro.data.question_get

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.question_get.model.asDomain
import org.softwaremaestro.data.question_get.remote.QuestionGetApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import javax.inject.Inject

class QuestionGetRepositoryImpl @Inject constructor(private val questionGetApi: QuestionGetApi) :
    QuestionGetRepository {

    override suspend fun getQuestion(): Flow<BaseResult<QuestionGetResultVO, String>> {
        return flow {
            val response = questionGetApi.getQuestion()
            if (response.isSuccessful) {
                response.body()!!.data?.let {
                    emit(BaseResult.Success(it.asDomain()))
                }
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
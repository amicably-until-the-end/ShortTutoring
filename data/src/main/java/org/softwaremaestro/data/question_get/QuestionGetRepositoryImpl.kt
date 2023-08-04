package org.softwaremaestro.data.question_get

import android.util.Log
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

    override suspend fun getQuestions(): Flow<BaseResult<List<QuestionGetResultVO>, String>> {
        return flow {
            val response = questionGetApi.getQuestions()
            if (response.isSuccessful && response.body()?.error == false) {
                response.body()!!.data
                    ?.map { it.asDomain() }
                    ?.let { emit(BaseResult.Success(it)) }
            } else {
                val errorString =
                    "error in ${this@QuestionGetRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()!!.message}"
                Log.d("Error", errorString)
            }
        }
    }
}
package org.softwaremaestro.data.question_check

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.question_check.model.QuestionCheckRequestDto
import org.softwaremaestro.data.question_check.model.asDomain
import org.softwaremaestro.data.question_check.remote.QuestionCheckApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_check.QuestionCheckRepository
import org.softwaremaestro.domain.question_check.entity.QuestionCheckRequestVO
import org.softwaremaestro.domain.question_check.entity.QuestionCheckResultVO
import javax.inject.Inject

class QuestionCheckRepositoryImpl @Inject constructor(private val questionCheckApi: QuestionCheckApi) :
    QuestionCheckRepository {

    override suspend fun checkQuestion(
        requestId: String,
        questionCheckRequestVO: QuestionCheckRequestVO
    ): Flow<BaseResult<QuestionCheckResultVO, String>> {
        return flow {
            val dto = QuestionCheckRequestDto(questionCheckRequestVO.teacherId)
            val response = questionCheckApi.checkQuestion(requestId, dto)
            if (response.isSuccessful) {
                Log.d("Result in raw", response.toString())
                val vo = response.body()!!.data?.asDomain()
                if (vo != null)
                    emit(BaseResult.Success(vo))
            } else {
                val errorString =
                    "error in ${this@QuestionCheckRepositoryImpl::class.java.name}\n" +
                            "message: ${response.message()}"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
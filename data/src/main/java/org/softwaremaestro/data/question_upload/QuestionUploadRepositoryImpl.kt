package org.softwaremaestro.data.question_upload

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.question_upload.model.QuestionUploadRequestDto
import org.softwaremaestro.data.question_upload.remote.QuestionUploadApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import javax.inject.Inject

class QuestionUploadRepositoryImpl @Inject constructor(private val questionUploadApi: QuestionUploadApi) :
    QuestionUploadRepository {


    override suspend fun uploadQuestion(questionUploadVO: QuestionUploadVO): Flow<BaseResult<QuestionUploadResultVO, String>> {
        return flow {
            val dto = QuestionUploadRequestDto(
                questionUploadVO.title,
                questionUploadVO.detail
            )
            val response = questionUploadApi.uploadQuestion(dto)
            if (response.isSuccessful) {
                val body = response.body()!!
                val resultVO = QuestionUploadResultVO(body.data?.questionId!!)
                emit(BaseResult.Success(resultVO))
            } else {
                val errorString = "error"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
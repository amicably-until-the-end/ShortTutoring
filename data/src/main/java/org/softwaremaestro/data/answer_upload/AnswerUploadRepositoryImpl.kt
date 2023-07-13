package org.softwaremaestro.data.answer_upload

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.answer_upload.model.AnswerUploadRequestDto
import org.softwaremaestro.data.answer_upload.model.TeacherDto
import org.softwaremaestro.data.answer_upload.model.asDomain
import org.softwaremaestro.data.answer_upload.remote.AnswerUploadApi
import org.softwaremaestro.domain.answer_upload.AnswerUploadRepository
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadResultVO
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class AnswerUploadRepositoryImpl @Inject constructor(private val answerUploadApi: AnswerUploadApi): AnswerUploadRepository {
    override suspend fun uploadAnswer(answerUploadVO: AnswerUploadVO): Flow<BaseResult<AnswerUploadResultVO, String>> {
        return flow {
            val dto = AnswerUploadRequestDto(
                answerUploadVO.id,
                TeacherDto(answerUploadVO.teacherVO.teacherId)
            )
            val response = answerUploadApi.uploadAnswer(dto.id, dto.teacherDto)
            if (response.isSuccessful) {
                response.body()!!.asDomain().let {
                    emit(BaseResult.Success(it))
                }
            }
            else {
                val errorString =
                    "error in ${this@AnswerUploadRepositoryImpl::class.java.name}\n" +
                            "message: ${response.message()}"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
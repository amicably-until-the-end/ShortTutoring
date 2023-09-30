package org.softwaremaestro.data.answer_upload

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.answer_upload.model.AnswerUploadRequestDto
import org.softwaremaestro.data.answer_upload.model.StudentPickReqDto
import org.softwaremaestro.data.answer_upload.model.asDomain
import org.softwaremaestro.data.answer_upload.remote.AnswerUploadApi
import org.softwaremaestro.data.common.utils.toStringWithTimeZone
import org.softwaremaestro.domain.answer_upload.AnswerUploadRepository
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadResultVO
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.answer_upload.entity.StudentPickResultVO
import org.softwaremaestro.domain.common.BaseResult
import java.time.LocalDateTime
import javax.inject.Inject

class AnswerUploadRepositoryImpl @Inject constructor(private val answerUploadApi: AnswerUploadApi) :
    AnswerUploadRepository {
    override suspend fun uploadAnswer(answerUploadVO: AnswerUploadVO): Flow<BaseResult<AnswerUploadResultVO, String>> {
        return flow {
            val dto = AnswerUploadRequestDto(
                answerUploadVO.requestId
            )
            val response = answerUploadApi.uploadAnswer(dto.id)
            val body = response.body()!!
            if (body.success == true) {
                response.body()!!.data?.asDomain()?.let {
                    emit(BaseResult.Success(it))
                }
            } else {
                val errorString =
                    "error in ${this@AnswerUploadRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()?.message}"
                emit(BaseResult.Error(errorString))
            }
        }
    }

    override suspend fun pickStudent(
        questionId: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        chattingId: String
    ): Flow<BaseResult<StudentPickResultVO, String>> {
        return flow {
            val response = answerUploadApi.pickStudent(
                questionId, StudentPickReqDto(
                    startTime = startTime.toStringWithTimeZone(),
                    endTime = endTime.toStringWithTimeZone(),
                    chattingId = chattingId
                )
            )
            val body = response.body()!!
            if (body.success!!) {
                body.data?.asDomain()?.let {
                    emit(BaseResult.Success(it))
                }
            } else {
                val errorString =
                    "error in ${this@AnswerUploadRepositoryImpl::class.java.name}\n" +
                            "message: ${response.body()?.message}"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
package org.softwaremaestro.domain.answer_upload

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadResultVO
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.answer_upload.entity.StudentPickResultVO
import org.softwaremaestro.domain.common.BaseResult

interface AnswerUploadRepository {
    suspend fun uploadAnswer(answerUploadVO: AnswerUploadVO): Flow<BaseResult<AnswerUploadResultVO, String>>

    suspend fun pickStudent(questionId: String): Flow<BaseResult<StudentPickResultVO, String>>
}
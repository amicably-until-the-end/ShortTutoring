package org.softwaremaestro.domain.answer_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.answer_upload.AnswerUploadRepository
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadResultVO
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import javax.inject.Inject

class AnswerUploadUseCase @Inject constructor(private val repository: AnswerUploadRepository) {
    suspend fun execute(answerUploadVO: AnswerUploadVO): Flow<BaseResult<AnswerUploadResultVO, String>> {
        return repository.uploadAnswer(answerUploadVO)
    }
}
package org.softwaremaestro.domain.answer_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.answer_upload.AnswerUploadRepository
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadResVO
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class AnswerUploadUseCase @Inject constructor(private val repository: AnswerUploadRepository) {
    suspend fun execute(answerUploadVO: AnswerUploadVO): Flow<BaseResult<AnswerUploadResVO, String>> {
        return repository.uploadAnswer(answerUploadVO)
    }
}
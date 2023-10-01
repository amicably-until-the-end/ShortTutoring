package org.softwaremaestro.domain.answer_upload.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.answer_upload.AnswerUploadRepository
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class DeclineQuestionUseCase @Inject constructor(private val repository: AnswerUploadRepository) {
    suspend fun execute(chattingId: String): Flow<BaseResult<Boolean, String>> {
        return repository.declineQuestion(chattingId)
    }
}
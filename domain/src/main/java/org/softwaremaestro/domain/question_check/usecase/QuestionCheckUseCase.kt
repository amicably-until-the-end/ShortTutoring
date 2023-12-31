package org.softwaremaestro.domain.question_check.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_check.QuestionCheckRepository
import org.softwaremaestro.domain.question_check.entity.QuestionCheckRequestVO
import org.softwaremaestro.domain.question_check.entity.QuestionCheckResultVO
import javax.inject.Inject

class QuestionCheckUseCase @Inject constructor(private val repository: QuestionCheckRepository) {

    suspend fun execute(
        requestId: String,
        questionCheckRequestVO: QuestionCheckRequestVO
    ): Flow<BaseResult<QuestionCheckResultVO, String>> {
        return repository.checkQuestion(requestId, questionCheckRequestVO)
    }
}
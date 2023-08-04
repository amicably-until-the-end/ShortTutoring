package org.softwaremaestro.domain.question_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import javax.inject.Inject

class QuestionGetUseCase @Inject constructor(private val repository: QuestionGetRepository) {

    suspend fun execute(): Flow<BaseResult<List<QuestionGetResponseVO>, String>> {
        return repository.getQuestions()
    }
}
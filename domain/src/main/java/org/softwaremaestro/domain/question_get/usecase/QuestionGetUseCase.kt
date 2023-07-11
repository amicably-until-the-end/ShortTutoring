package org.softwaremaestro.domain.question_get.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import javax.inject.Inject

class QuestionGetUseCase @Inject constructor(private val repository: QuestionGetRepository) {

    suspend fun execute(): Flow<BaseResult<List<QuestionGetResultVO>, String>> {
        return repository.getQuestions()
    }
}
package org.softwaremaestro.domain.question_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO


interface QuestionGetRepository {
    suspend fun getQuestions(): Flow<BaseResult<List<QuestionGetResponseVO>, String>>
    suspend fun getQuestionInfo(questionId: String): Flow<BaseResult<QuestionGetResponseVO, String>>
}
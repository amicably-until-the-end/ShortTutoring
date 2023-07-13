package org.softwaremaestro.domain.question_check

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_check.entity.QuestionCheckRequestVO
import org.softwaremaestro.domain.question_check.entity.QuestionCheckResultVO
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO

interface QuestionCheckRepository {
    suspend fun checkQuestion(
        requestId: String,
        questionCheckRequestVO: QuestionCheckRequestVO
    ): Flow<BaseResult<QuestionCheckResultVO, String>>
}
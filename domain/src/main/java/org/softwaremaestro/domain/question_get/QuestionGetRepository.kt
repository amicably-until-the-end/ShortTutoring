package org.softwaremaestro.domain.question_get

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO


interface QuestionGetRepository {
    suspend fun getQuestion(): Flow<BaseResult<QuestionGetResultVO, String>>
}
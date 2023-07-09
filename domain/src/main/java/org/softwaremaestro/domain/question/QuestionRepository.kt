package org.softwaremaestro.domain.question

import org.softwaremaestro.domain.question.entity.QuestionVO

interface QuestionRepository
{
    suspend fun login(questionVO : QuestionVO)
}
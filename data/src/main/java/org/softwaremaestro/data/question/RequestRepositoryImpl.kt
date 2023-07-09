package org.softwaremaestro.data.question

import org.softwaremaestro.domain.question.QuestionRepository
import org.softwaremaestro.domain.question.entity.QuestionVO

import javax.inject.Inject

class RequestRepositoryImpl @Inject constructor(): QuestionRepository {
    override suspend fun login(questionVO: QuestionVO) {
        TODO("Not yet implemented")
    }

}
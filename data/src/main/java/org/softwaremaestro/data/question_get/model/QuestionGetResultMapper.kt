package org.softwaremaestro.data.question_get.model

import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO

object QuestionGetResultMapper {
    fun asDomain(questionsGetResultDto: QuestionsGetResultDto): QuestionGetResultVO {
        return QuestionGetResultVO(
            questionsGetResultDto.id,
            questionsGetResultDto.problem?.imageUrl,
            questionsGetResultDto.problem?.schoolLevel,
            questionsGetResultDto.problem?.schoolLevel,
            questionsGetResultDto.problem?.schoolSubject,
            questionsGetResultDto.problem?.difficulty,
            questionsGetResultDto.problem?.description,
            questionsGetResultDto.status,
            questionsGetResultDto.tutoringId,
            questionsGetResultDto.createdAt
        )
    }
}

fun QuestionsGetResultDto.asDomain(): QuestionGetResultVO {
    return QuestionGetResultMapper.asDomain(this)
}
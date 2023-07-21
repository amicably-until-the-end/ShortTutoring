package org.softwaremaestro.data.question_get.model

import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO

private const val EMPTY_STRING = "-"

object QuestionGetResultMapper {
    fun asDomain(questionsGetResultDto: QuestionsGetResultDto): QuestionGetResultVO {
        return QuestionGetResultVO(
            questionsGetResultDto.id,
            questionsGetResultDto.student?.id,
            questionsGetResultDto.student?.name,
            questionsGetResultDto.problemDto?.imageUrl,
            questionsGetResultDto.problemDto?.schoolLevel,
            questionsGetResultDto.problemDto?.schoolChapter,
            questionsGetResultDto.problemDto?.schoolSubject,
            questionsGetResultDto.problemDto?.difficulty,
            questionsGetResultDto.problemDto?.description,
            questionsGetResultDto.teacherIds
        )
    }
}

fun QuestionsGetResultDto.asDomain(): QuestionGetResultVO {
    return QuestionGetResultMapper.asDomain(this)
}
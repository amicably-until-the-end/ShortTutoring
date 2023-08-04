package org.softwaremaestro.data.question_get.model

import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO

private const val EMPTY_STRING = "-"

object QuestionGetResultMapper {
    fun asDomain(questionsGetResultDto: QuestionsGetResultDto): QuestionGetResultVO {
        with(questionsGetResultDto) {
            return QuestionGetResultVO(
                id,
                student?.id,
                student?.name,
                student?.profileImage,
                problemDto?.image,
                problemDto?.schoolLevel,
                problemDto?.schoolSubject,
                problemDto?.difficulty,
                problemDto?.description,
                teacherIds
            )
        }
    }
}

fun QuestionsGetResultDto.asDomain(): QuestionGetResultVO {
    return QuestionGetResultMapper.asDomain(this)
}
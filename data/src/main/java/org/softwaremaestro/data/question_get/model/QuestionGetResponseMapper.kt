package org.softwaremaestro.data.question_get.model

import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO

private const val EMPTY_STRING = "-"

object QuestionGetResultMapper {
    fun asDomain(questionGetResponseDto: QuestionGetResponseDto): QuestionGetResponseVO {
        with(questionGetResponseDto) {
            return QuestionGetResponseVO(
                id,
                student?.id,
                student?.name,
                student?.profileImage,
                problemDto?.image,
                problemDto?.schoolLevel,
                problemDto?.schoolSubject,
                problemDto?.difficulty,
                problemDto?.description,
                teacherIds,
                status,
                createdAt,
                tutoringId
            )
        }
    }
}

fun QuestionGetResponseDto.asDomain(): QuestionGetResponseVO {
    return QuestionGetResultMapper.asDomain(this)
}
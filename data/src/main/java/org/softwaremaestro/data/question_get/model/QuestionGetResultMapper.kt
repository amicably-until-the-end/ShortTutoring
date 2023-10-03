package org.softwaremaestro.data.question_get.model

import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO

private const val EMPTY_STRING = "-"

object QuestionGetResultMapper {
    fun asDomain(questionGetResponseDto: QuestionsGetResultDto): QuestionGetResponseVO {
        with(questionGetResponseDto) {
            return QuestionGetResponseVO(
                id = id,
                studentId = student?.id,
                studentName = student?.name,
                studentProfileImage = student?.profileImage,
                images = problemDto?.image,
                mainImage = problemDto?.mainImage,
                hopeTutoringTime = hopeTutoringTime,
                hopeImmediately = hopeImmediately,
                problemSchoolLevel = problemDto?.schoolLevel,
                problemSubject = problemDto?.schoolSubject,
                createdAt = createdAt,
                problemDescription = problemDto?.description,
                offerTeachers = offerTeachers,
                status = status,
                tutoringId = tutoringId
            )
        }
    }
}

fun QuestionsGetResultDto.asDomain(): QuestionGetResponseVO {
    return QuestionGetResultMapper.asDomain(this)
}
package org.softwaremaestro.data.question

import org.softwaremaestro.data.question.dto.QuestionDto
import org.softwaremaestro.domain.question.entity.QuestionVO

object QuestionMapper {
    fun asDomain(questionDto: QuestionDto): QuestionVO {
        return QuestionVO(questionDto.studentId,questionDto.studentName)
    }
}

fun QuestionDto.asDomain(): QuestionVO {
    return QuestionMapper.asDomain(this)
}
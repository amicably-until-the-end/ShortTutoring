package org.softwaremaestro.data.question_get.model

import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO

object QuestionGetResultMapper {
    fun asDomain(questionGetResultDto: QuestionGetResultDto): QuestionGetResultVO {
        return QuestionGetResultVO(
            questionGetResultDto.problemBase64Image,
            questionGetResultDto.problemSchoolLevel,
            questionGetResultDto.problemSchoolChapter,
            questionGetResultDto.problemSchoolSubject,
            questionGetResultDto.problemDifficulty,
            questionGetResultDto.problemDescription,
            questionGetResultDto.reviews,
            questionGetResultDto.teacherIds,
            questionGetResultDto.studentId,
        )
    }
}

fun QuestionGetResultDto.asDomain(): QuestionGetResultVO {
    return QuestionGetResultMapper.asDomain(this)
}
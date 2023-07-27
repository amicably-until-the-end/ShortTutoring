package org.softwaremaestro.data.question_check.model

import org.softwaremaestro.data.question_upload.model.QuestionMapper
import org.softwaremaestro.data.question_upload.model.QuestionUploadResultDto
import org.softwaremaestro.domain.question_check.entity.QuestionCheckResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO

object QuestionCheckResponseMapper {
    fun asDomain(questionCheckResponseDto: QuestionCheckResponseDto): QuestionCheckResultVO {
        return QuestionCheckResultVO(
            questionCheckResponseDto.status,
            questionCheckResponseDto.tutoringId,
            questionCheckResponseDto.whiteBoardToken,
            questionCheckResponseDto.whiteBoardUUID,
            questionCheckResponseDto.whiteBoardAppId
        )
    }
}

fun QuestionCheckResponseDto.asDomain(): QuestionCheckResultVO {
    return QuestionCheckResponseMapper.asDomain(this)
}
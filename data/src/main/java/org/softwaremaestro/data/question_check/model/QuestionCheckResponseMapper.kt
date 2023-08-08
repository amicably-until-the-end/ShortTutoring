package org.softwaremaestro.data.question_check.model

import org.softwaremaestro.data.question_upload.model.QuestionMapper
import org.softwaremaestro.data.question_upload.model.QuestionUploadResultDto
import org.softwaremaestro.domain.question_check.entity.QuestionCheckResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO

object QuestionCheckResponseMapper {
    fun asDomain(questionCheckResponseDto: QuestionCheckResponseDto): QuestionCheckResultVO {
        return QuestionCheckResultVO(
            status = questionCheckResponseDto.status,
            tutoringId = questionCheckResponseDto.tutoringId,
            whiteBoardToken = questionCheckResponseDto.whiteBoardToken,
            whiteBoardUUID = questionCheckResponseDto.whiteBoardUUID,
            whiteBoardAppId = questionCheckResponseDto.whiteBoardAppId,
            teacherRTCToken = questionCheckResponseDto.teacherRTCToken,
            studentRTCToken = questionCheckResponseDto.studentRTCToken,
            RTCAppId = questionCheckResponseDto.RTCAppId
        )
    }
}

fun QuestionCheckResponseDto.asDomain(): QuestionCheckResultVO {
    return QuestionCheckResponseMapper.asDomain(this)
}
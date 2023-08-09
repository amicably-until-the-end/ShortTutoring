package org.softwaremaestro.data.question_check.model

import org.softwaremaestro.data.question_upload.model.QuestionMapper
import org.softwaremaestro.data.question_upload.model.QuestionUploadResultDto
import org.softwaremaestro.domain.question_check.entity.QuestionCheckResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO

object QuestionCheckResponseMapper {
    fun asDomain(questionCheckResponseDto: QuestionCheckResponseDto): QuestionCheckResultVO {
        val tutoring = questionCheckResponseDto.tutoring
        return QuestionCheckResultVO(
            status = tutoring.status,
            tutoringId = tutoring.id,
            whiteBoardToken = tutoring.whiteBoardToken,
            whiteBoardUUID = tutoring.whiteBoardUUID,
            whiteBoardAppId = tutoring.whiteBoardAppId,
            teacherRTCToken = tutoring.teacherRTCToken,
            studentRTCToken = tutoring.studentRTCToken,
            RTCAppId = tutoring.RTCAppId,
            studentSelect = questionCheckResponseDto.status
        )
    }
}

fun QuestionCheckResponseDto.asDomain(): QuestionCheckResultVO {
    return QuestionCheckResponseMapper.asDomain(this)
}
package org.softwaremaestro.data.question_upload.model

import org.softwaremaestro.domain.question_upload.entity.QuestionUploadResultVO
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO

object QuestionMapper {
    fun asDomain(questionUploadResultDto: QuestionUploadResultDto): QuestionUploadResultVO {
        return QuestionUploadResultVO(questionUploadResultDto.questionId)
    }
}

fun QuestionUploadResultDto.asDomain(): QuestionUploadResultVO {
    return QuestionMapper.asDomain(this)
}
package org.softwaremaestro.data.answer_upload.model

import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadResultVO

object AnswerUploadResultMapper {
    fun asDomain(answerUploadResultDto: AnswerUploadResultDto): AnswerUploadResultVO {
        return AnswerUploadResultVO(
            chatRoomId = answerUploadResultDto.chatRoomId
        )
    }
}

fun AnswerUploadResultDto.asDomain(): AnswerUploadResultVO {
    return AnswerUploadResultMapper.asDomain(this)
}
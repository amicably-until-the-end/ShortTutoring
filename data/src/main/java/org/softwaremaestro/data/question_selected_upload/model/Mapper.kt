package org.softwaremaestro.data.question_selected_upload.model

import org.softwaremaestro.data.common.utils.toStringWithTimeZone
import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadVO

object Mapper {
    fun questionSelectedUploadVOAsDto(questionSelectedUploadVO: QuestionSelectedUploadVO): QuestionSelectedUploadReqDto {
        return with(questionSelectedUploadVO) {
            QuestionSelectedUploadReqDto(
                description = description,
                schoolLevel = schoolLevel,
                schoolSubject = schoolSubject,
                mainImageIndex = mainImageIndex,
                images = images,
                requestTutoringStartTime = requestTutoringStartTime.toStringWithTimeZone(),
                requestTutoringEndTime = requestTutoringEndTime.toStringWithTimeZone(),
                requestTeacherId = requestTeacherId
            )
        }
    }

}

fun QuestionSelectedUploadVO.asDto(): QuestionSelectedUploadReqDto {
    return Mapper.questionSelectedUploadVOAsDto(this)
}

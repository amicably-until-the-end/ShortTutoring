package org.softwaremaestro.data.question_selected_upload.model

import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadVO

object Mapper {
    fun questionSelectedUploadVOAsDto(questionSelectedUploadVO: QuestionSelectedUploadVO): QuestionSelectedUploadReqDto {
        return with(questionSelectedUploadVO) {
            QuestionSelectedUploadReqDto(
                schoolLevel,
                description,
                schoolSubject,
                mainImageIndex,
                images,
                requestTutoringStartTime,
                requestTutoringEndTime,
                requestTeacherId
            )
        }
    }

}

fun QuestionSelectedUploadVO.asDto(): QuestionSelectedUploadReqDto {
    return Mapper.questionSelectedUploadVOAsDto(this)
}

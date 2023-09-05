package org.softwaremaestro.data.question_upload.model

import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.domain.question_upload.entity.TeacherPickResVO

object Mapper {
    fun pickTeacherResDtoAsDomain(pickTeacherResDto: PickTeacherResDto): TeacherPickResVO {
        return TeacherPickResVO(
            tutoringId = pickTeacherResDto.tutoringId,
            whiteBoardToken = pickTeacherResDto.whiteBoardToken,
            whiteBoardUUID = pickTeacherResDto.whiteBoardUUID,
            whiteBoardAppId = pickTeacherResDto.whiteBoardAppId,
            teacherRTCToken = pickTeacherResDto.teacherRTCToken,
            studentRTCToken = pickTeacherResDto.studentRTCToken,
            RTCAppId = pickTeacherResDto.RTCAppId
        )
    }

    fun questionUploadVOAsDto(questionUploadVO: QuestionUploadVO): QuestionUploadRequestDto {
        return QuestionUploadRequestDto(
            images = questionUploadVO.images,
            description = questionUploadVO.description,
            schoolLevel = questionUploadVO.schoolLevel,
            schoolSubject = questionUploadVO.schoolSubject
        )
    }

}

fun PickTeacherResDto.asDomain(): TeacherPickResVO {
    return Mapper.pickTeacherResDtoAsDomain(this)
}

fun QuestionUploadVO.asDto(): QuestionUploadRequestDto {
    return Mapper.questionUploadVOAsDto(this)
}

package org.softwaremaestro.data.question_upload.model

import org.softwaremaestro.domain.question_upload.entity.TeacherPickResVO

object Mapper {
    fun PickTeacherResDtoAsDomain(pickTeacherResDto: PickTeacherResDto): TeacherPickResVO {
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
}

fun PickTeacherResDto.asDomain(): TeacherPickResVO {
    return Mapper.PickTeacherResDtoAsDomain(this)
}
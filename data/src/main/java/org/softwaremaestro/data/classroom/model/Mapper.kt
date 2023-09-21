package org.softwaremaestro.data.classroom.model

import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO

class Mapper {
    fun asDomain(tutoringInfoDto: TutoringInfoDto): TutoringInfoVO {
        tutoringInfoDto.apply {
            return TutoringInfoVO(
                id = id,
                teacherId = teacherId,
                studentId = studentId,
                status = status,
                matchedAt = matchedAt,
                endedAt = endedAt,
                whiteBoardAppId = whiteBoardAppId,
                whiteBoardToken = whiteBoardToken,
                whiteBoardUUID = whiteBoardUUID,
                RTCAppId = RTCAppId,
                studentRTCToken = studentRTCToken,
                teacherRTCToken = teacherRTCToken,
            )

        }
    }
}

fun TutoringInfoDto.asDomain(): TutoringInfoVO {
    return Mapper().asDomain(this)
}
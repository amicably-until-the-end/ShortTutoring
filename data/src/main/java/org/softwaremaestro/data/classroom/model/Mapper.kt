package org.softwaremaestro.data.classroom.model

import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO

class Mapper {
    fun asDomain(tutoringInfoDto: TutoringInfoDto): TutoringInfoVO {
        return TutoringInfoVO(
            tutoringInfoDto.tutoringId,
            tutoringInfoDto.requestId,
            tutoringInfoDto.teacherId,
            tutoringInfoDto.studentId,
            tutoringInfoDto.status,
            tutoringInfoDto.matchedAt,
            tutoringInfoDto.endedAt,
            tutoringInfoDto.whiteBoardAppId,
            tutoringInfoDto.whiteBoardToken,
            tutoringInfoDto.whiteBoardUUID
        )
    }
}

fun TutoringInfoDto.asDomain(): TutoringInfoVO {
    return Mapper().asDomain(this)
}
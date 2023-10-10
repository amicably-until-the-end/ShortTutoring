package org.softwaremaestro.data.classroom.model

import org.softwaremaestro.data.common.utils.parseToLocalDateTime
import org.softwaremaestro.domain.classroom.entity.ClassroomInfoVO
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO

class Mapper {
    fun asDomain(tutoringInfoDto: TutoringInfoDto): TutoringInfoVO {
        tutoringInfoDto.apply {
            return TutoringInfoVO(
                id = id,
                teacherId = teacherId,
                studentId = studentId,
                status = status,
                reservedEnd = reservedEnd.parseToLocalDateTime(),
                reservedStart = reservedStart.parseToLocalDateTime(),
            )

        }
    }

    fun asDomain(classroomInfoDto: ClassroomInfoDto): ClassroomInfoVO {
        classroomInfoDto.apply {
            return ClassroomInfoVO(
                boardAppId = id,
                boardUUID = studentId,
                boardToken = status,
                rtcToken = rtcToken,
                rtcChannel = rtcChannel,
                rtcAppId = rtcAppId,
                rtcUID = rtcUID,
            )
        }
    }
}

fun TutoringInfoDto.asDomain(): TutoringInfoVO {
    return Mapper().asDomain(this)
}

fun ClassroomInfoDto.asDomain(): ClassroomInfoVO {
    return Mapper().asDomain(this)
}
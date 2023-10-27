package org.softwaremaestro.data.tutoring_get.model

import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO

object Mapper {
    fun asDomain(tutoringGetResDto: TutoringGetResDto): TutoringVO {
        with(tutoringGetResDto) {
            return TutoringVO(
                description = description,
                schoolSubject = schoolSubject,
                recordFileUrl = recordFileUrl,
                tutoringId = tutoringId,
                questionId = questionId,
                schoolLevel = schoolLevel,
                tutoringDate = tutoringDate,
                opponentName = opponentName,
                opponentProfileImage = opponentProfileImage,
                opponentId = opponentId,
                questionImage = questionImage
            )
        }
    }
}

fun TutoringGetResDto.asDomain(): TutoringVO {
    return Mapper.asDomain(this)
}
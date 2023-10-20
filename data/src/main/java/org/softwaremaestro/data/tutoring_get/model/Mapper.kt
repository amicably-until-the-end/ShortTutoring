package org.softwaremaestro.data.tutoring_get.model

import org.softwaremaestro.data.common.utils.parseToLocalDateTime
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import java.time.LocalTime

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
                questionImage = questionImage
            )
        }
    }
}

fun TutoringGetResDto.asDomain(): TutoringVO {
    return Mapper.asDomain(this)
}
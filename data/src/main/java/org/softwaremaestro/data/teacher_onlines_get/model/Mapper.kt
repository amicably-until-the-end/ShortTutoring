package org.softwaremaestro.data.teacher_onlines_get.model

import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO

object Mapper {
    fun asDomain(teacherOnlineDto: TeacherOnlineDto): TeacherVO {
        with(teacherOnlineDto) {
            return TeacherVO(
                teacherId = id,
                nickname = name,
                profileUrl = profileImage,
                followers = followers,
                reservationCnt = reserveCnt,
                bio = bio,
                rating = rating,
                major = major,
                univ = univ,
            )
        }
    }
}

fun TeacherOnlineDto.asDomain(): TeacherVO {
    return Mapper.asDomain(this)
}
package org.softwaremaestro.data.teacher_onlines_get.model

import org.softwaremaestro.domain.teacher_onlines_get.entity.TeacherOnlineVO

object Mapper {
    fun asDomain(teacherOnlineDto: TeacherOnlineDto): TeacherOnlineVO {
        with(teacherOnlineDto) {
            return TeacherOnlineVO(
                id = id,
                name = name,
                profileImage = profileImage,
                followers = followers
            )
        }
    }
}

fun TeacherOnlineDto.asDomain(): TeacherOnlineVO {
    return Mapper.asDomain(this)
}
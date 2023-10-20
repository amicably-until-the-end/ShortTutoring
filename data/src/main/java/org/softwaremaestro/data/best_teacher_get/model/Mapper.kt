package org.softwaremaestro.data.best_teacher_get.model

import org.softwaremaestro.domain.best_teacher_get.entity.TeacherVO

object Mapper {
    fun asDomain(bestTeacherGetResDto: BestTeacherGetResDto): TeacherVO {
        with(bestTeacherGetResDto) {
            return TeacherVO(
                profileUrl = profileImage,
                nickname = name,
                teacherId = id,
                bio = bio,
                univ = school?.schoolName,
                major = school?.schoolDepartment,
                rating = rating,
                followers = followers,
                reservationCnt = -1
            )
        }
    }
}

fun BestTeacherGetResDto.asDomain(): TeacherVO {
    return Mapper.asDomain(this)
}
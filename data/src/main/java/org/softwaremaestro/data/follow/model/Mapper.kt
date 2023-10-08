package org.softwaremaestro.data.follow.model

import org.softwaremaestro.domain.follow.entity.FollowerGetResponseVO
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO

object Mapper {
    fun asDomain(follwerGetResponseDto: FollowerGetResponseDto): FollowerGetResponseVO {
        with(follwerGetResponseDto) {
            return FollowerGetResponseVO(
                id,
                name,
                bio,
                profileImage,
                role,
                school?.level,
                school?.grade,
                followersCount,
                followingCount
            )
        }
    }

    fun asDomain(followingGetResponseDto: FollowingGetResponseDto): TeacherVO {
        with(followingGetResponseDto) {
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

fun FollowerGetResponseDto.asDomain(): FollowerGetResponseVO {
    return Mapper.asDomain(this)
}

fun FollowingGetResponseDto.asDomain(): TeacherVO {
    return Mapper.asDomain(this)
}
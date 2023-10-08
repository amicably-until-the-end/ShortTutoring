package org.softwaremaestro.data.follow.model

import org.softwaremaestro.domain.follow.entity.FollowerGetResponseVO
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO

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

    fun asDomain(followingGetResponseDto: FollowingGetResponseDto): FollowingGetResponseVO {
        with(followingGetResponseDto) {
            return FollowingGetResponseVO(
                id,
                name,
                bio,
                profileImage,
                role,
                "",
                school?.schoolName,
                school?.schoolDepartment,
                followers,
                followingCount
            )
        }
    }
}

fun FollowerGetResponseDto.asDomain(): FollowerGetResponseVO {
    return Mapper.asDomain(this)
}

fun FollowingGetResponseDto.asDomain(): FollowingGetResponseVO {
    return Mapper.asDomain(this)
}
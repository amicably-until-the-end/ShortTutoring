package org.softwaremaestro.data.follower_get.model

import org.softwaremaestro.domain.follower_get.entity.FollowerGetResponseVO

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
}

fun FollowerGetResponseDto.asDomain(): FollowerGetResponseVO {
    return Mapper.asDomain(this)
}
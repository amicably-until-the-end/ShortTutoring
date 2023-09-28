package org.softwaremaestro.data.following_get.model

import org.softwaremaestro.domain.following_get.entity.FollowingGetResponseVO

private const val EMPTY_STRING = "-"

object FollowingGetResponseMapper {
    fun asDomain(followingGetResponseDto: FollowingGetResponseDto): FollowingGetResponseVO {
        with(followingGetResponseDto) {
            return FollowingGetResponseVO(
                id,
                name,
                bio,
                profileImage,
                role,
                school?.division,
                school?.name,
                school?.department,
                school?.grade,
                followersCount,
                followingCount
            )
        }
    }
}

fun FollowingGetResponseDto.asDomain(): FollowingGetResponseVO {
    return FollowingGetResponseMapper.asDomain(this)
}
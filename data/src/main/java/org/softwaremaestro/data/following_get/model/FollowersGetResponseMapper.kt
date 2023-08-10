package org.softwaremaestro.data.following_get.model

import org.softwaremaestro.domain.following_get.entity.FollowingGetResponseVO

private const val EMPTY_STRING = "-"

object FollowersGetResponseMapper {
    fun asDomain(follwersGetResponseDto: FollowingGetResponseDto): FollowingGetResponseVO {
        with(follwersGetResponseDto) {
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
    return FollowersGetResponseMapper.asDomain(this)
}
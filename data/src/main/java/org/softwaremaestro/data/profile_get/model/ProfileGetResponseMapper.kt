package org.softwaremaestro.data.profile_get.model

import org.softwaremaestro.domain.profile_get.entity.ProfileGetResponseVO

object ProfileGetResultMapper {
    fun asDomain(profileGetResponseDto: ProfileGetResponseDto): ProfileGetResponseVO {
        with(profileGetResponseDto) {
            return ProfileGetResponseVO(
                following,
                school?.level,
                school?.grade,
                role,
                followers,
                bio,
                id,
                name,
                profileImage
            )
        }
    }
}

fun ProfileGetResponseDto.asDomain(): ProfileGetResponseVO {
    return ProfileGetResultMapper.asDomain(this)
}
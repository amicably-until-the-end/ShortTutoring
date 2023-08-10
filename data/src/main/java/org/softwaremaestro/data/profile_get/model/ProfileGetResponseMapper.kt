package org.softwaremaestro.data.profile_get.model

import org.softwaremaestro.domain.profile_get.entity.ProfileGetResponseVO

object ProfileGetResultMapper {
    fun asDomain(profileGetResponseDto: ProfileGetResponseDto): ProfileGetResponseVO {
        with(profileGetResponseDto) {
            return ProfileGetResponseVO(
                id,
                name,
                bio,
                profileImage,
                role,
                school?.level,
                school?.grade,
                school?.schoolDivision,
                school?.schoolName,
                school?.schoolDepartment,
                followersCount,
                followingCount
            )
        }
    }
}

fun ProfileGetResponseDto.asDomain(): ProfileGetResponseVO {
    return ProfileGetResultMapper.asDomain(this)
}
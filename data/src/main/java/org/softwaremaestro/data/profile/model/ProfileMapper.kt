package org.softwaremaestro.data.profile.model

import org.softwaremaestro.domain.profile.entity.MyProfileGetResponseVO
import org.softwaremaestro.domain.profile.entity.ProfileGetResponseVO
import org.softwaremaestro.domain.profile.entity.ProfileUpdateResponseVO

object ProfileMapper {
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

    fun asDomain(profileUpdateResponseDto: ProfileUpdateResponseDto): ProfileUpdateResponseVO {
        with(profileUpdateResponseDto) {
            return ProfileUpdateResponseVO(
                id,
                name,
                bio,
                profileImageBase64,
                profileImageFormat,
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

    fun asDomain(myProfileGetResponseDto: MyProfileGetResponseDto): MyProfileGetResponseVO {
        with(myProfileGetResponseDto) {
            return MyProfileGetResponseVO(
                following,
                school?.level,
                school?.grade,
                school?.schoolDivision,
                school?.schoolName,
                school?.schoolDepartment,
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
    return ProfileMapper.asDomain(this)
}

fun ProfileUpdateResponseDto.asDomain(): ProfileUpdateResponseVO {
    return ProfileMapper.asDomain(this)
}

fun MyProfileGetResponseDto.asDomain(): MyProfileGetResponseVO {
    return ProfileMapper.asDomain(this)
}

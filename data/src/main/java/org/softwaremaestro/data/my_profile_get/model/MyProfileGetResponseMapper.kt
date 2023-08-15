package org.softwaremaestro.data.my_profile_get.model

import org.softwaremaestro.domain.my_profile_get.entity.MyProfileGetResponseVO

object MyProfileGetResultMapper {
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

fun MyProfileGetResponseDto.asDomain(): MyProfileGetResponseVO {
    return MyProfileGetResultMapper.asDomain(this)
}
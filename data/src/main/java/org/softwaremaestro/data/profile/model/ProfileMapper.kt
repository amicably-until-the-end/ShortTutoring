package org.softwaremaestro.data.profile.model

import org.softwaremaestro.domain.profile.entity.MyProfileResVO
import org.softwaremaestro.domain.profile.entity.ProfileResVO

object ProfileMapper {
    fun asDomain(profileResDto: ProfileResDto): ProfileResVO {
        with(profileResDto) {
            return ProfileResVO(
                id = id,
                name = name,
                bio = bio,
                profileImage = profileImage,
                role = role,
                schoolLevel = school?.level,
                schoolGrade = school?.grade,
                schoolName = school?.schoolName,
                schoolDepartment = school?.schoolDepartment,
                followers = followers,
                followingCnt = followingCnt
            )
        }
    }

    fun asDomain(myProfileResDto: MyProfileResDto): MyProfileResVO {
        with(myProfileResDto) {
            return MyProfileResVO(
                id = id,
                name = name,
                bio = bio,
                profileImage = profileImage,
                role = role,
                schoolLevel = school?.level,
                schoolGrade = school?.grade,
                schoolName = school?.schoolName,
                schoolDepartment = school?.schoolDepartment,
                followers = followers,
                following = following,
                amount = coin?.amount,
                lastReceivedFreeCoinAt = coin?.lastReceivedFreeCoinAt
            )
        }
    }
}

fun ProfileResDto.asDomain(): ProfileResVO {
    return ProfileMapper.asDomain(this)
}

fun MyProfileResDto.asDomain(): MyProfileResVO {
    return ProfileMapper.asDomain(this)
}

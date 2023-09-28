package org.softwaremaestro.data.login.model

import org.softwaremaestro.domain.login.entity.UserVO

object Mapper {
    fun getUserInfoResDtoToUserVO(userInfoResDto: UserInfoResDto): UserVO {
        return UserVO(
            userInfoResDto.role,
            userInfoResDto.id,
            userInfoResDto.bio,
            userInfoResDto.name,
            userInfoResDto.profileImage,
        )
    }
}

fun UserInfoResDto.asDomain(): UserVO {
    return Mapper.getUserInfoResDtoToUserVO(this)
}
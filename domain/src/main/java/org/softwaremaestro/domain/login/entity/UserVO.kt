package org.softwaremaestro.domain.login.entity

data class UserVO(
    val role: String,
    val id: String,
    val bio: String,
    val name: String,
    val profileImage: String,
)
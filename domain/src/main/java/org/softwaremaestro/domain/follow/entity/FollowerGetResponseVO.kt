package org.softwaremaestro.domain.follow.entity

data class FollowerGetResponseVO(
    val id: String?,
    val name: String?,
    val profileImage: String?,
    val role: String?,
    val schoolLevel: String?,
    val grade: Int?,
//    val recentDate: String? = ""
)
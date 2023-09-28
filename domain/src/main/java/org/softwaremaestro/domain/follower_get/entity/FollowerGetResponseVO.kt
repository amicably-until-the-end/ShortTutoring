package org.softwaremaestro.domain.follower_get.entity

data class FollowerGetResponseVO(
    val id: String?,
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val role: String?,
    val school: String?,
    val grade: Int?,
//    val recentDate: String? = "",
    val followersCount: Int?,
    val followingCount: Int?
)
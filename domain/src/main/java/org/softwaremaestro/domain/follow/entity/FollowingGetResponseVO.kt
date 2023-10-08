package org.softwaremaestro.domain.follow.entity

data class FollowingGetResponseVO(
    val id: String?,
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val role: String?,
    val schoolDivision: String?,
    val schoolName: String?,
    val schoolDepartment: String?,
    val followers: List<String>?,
    val followingCount: Int?
)
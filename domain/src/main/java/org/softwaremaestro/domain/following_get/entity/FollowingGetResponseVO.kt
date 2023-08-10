package org.softwaremaestro.domain.following_get.entity

data class FollowingGetResponseVO(
    val id: String?,
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val role: String?,
    val schoolDivision: String?,
    val schoolName: String?,
    val schoolDepartment: String?,
    val schoolGrade: Int?,
    val followersCount: String?,
    val followingCount: String?
)
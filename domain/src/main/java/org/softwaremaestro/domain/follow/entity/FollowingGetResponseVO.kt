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
    val schoolGrade: Int?,
    val followersCount: Int?,
    val followingCount: Int?
)
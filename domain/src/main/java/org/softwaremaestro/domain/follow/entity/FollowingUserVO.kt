package org.softwaremaestro.domain.follow.entity

data class FollowingUserVO(
    val id: String?,
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val role: String?,
    val schoolDivision: String? = null,
    val schoolName: String? = null,
    val schoolDepartment: String? = null,
    val followers: List<String>?,
    val followingCount: Int? = null,
    val reserveCnt: Int? = null,
)
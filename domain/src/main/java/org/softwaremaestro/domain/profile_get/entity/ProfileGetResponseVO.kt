package org.softwaremaestro.domain.profile_get.entity

data class ProfileGetResponseVO(
    val id: String?,
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val role: String?,
    val schoolLevel: String?,
    val schoolGrade: Int?,
    val schoolDivision: String?,
    val schoolName: String?,
    val schoolDepartment: String?,
    val followersCount: Int?,
    val followingCount: Int?
)
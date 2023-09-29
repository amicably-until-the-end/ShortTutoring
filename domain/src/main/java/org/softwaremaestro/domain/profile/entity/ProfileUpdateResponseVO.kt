package org.softwaremaestro.domain.profile.entity

data class ProfileUpdateResponseVO(
    val id: String?,
    val name: String?,
    val bio: String?,
    val profileImageBase64: String?,
    val profileImageFormat: String?,
    val role: String?,
    val level: String?,
    val grade: Int?,
    val schoolDivision: String?,
    val schoolName: String?,
    val schoolDepartment: String?,
    val followersCount: Int?,
    val followingCount: Int?
)

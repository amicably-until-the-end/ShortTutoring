package org.softwaremaestro.domain.profile.entity

data class ProfileResVO(
    val schoolLevel: String?,
    val schoolGrade: Int?,
    val schoolName: String?,
    val schoolDepartment: String?,
    val role: String?,
    val followers: List<String>?,
    val followingCnt: Int?,
    val bio: String?,
    val id: String?,
    val name: String?,
    val profileImage: String?,
    val rating: Float?
)
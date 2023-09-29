package org.softwaremaestro.domain.profile.entity

data class MyProfileGetResponseVO(
    val following: List<String>?,
    val schoolLevel: String?,
    val schoolGrade: Int?,
    val schoolDivision: String?,
    val schoolName: String?,
    val schoolDepartment: String?,
    val role: String?,
    val followers: List<String>?,
    val bio: String?,
    val id: String?,
    val name: String?,
    val profileImage: String?
)
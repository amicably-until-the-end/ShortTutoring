package org.softwaremaestro.domain.profile.entity

data class MyProfileResVO(
    val id: String?,
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val role: String?,
    val schoolLevel: String?,
    val schoolGrade: Int?,
    val schoolName: String?,
    val schoolDepartment: String?,
    val followers: List<String>?,
    val following: List<String>?,
    val amount: Int?,
    val lastReceivedFreeCoinAt: String?
)
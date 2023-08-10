package org.softwaremaestro.domain.my_profile_get.entity

data class MyProfileGetResponseVO(
    val following: List<String>?,
    val schoolLevel: String?,
    val schoolGrade: Int?,
    val role: String?,
    val followers: List<String>?,
    val bio: String?,
    val id: String?,
    val name: String?,
    val profileImage: String?
)
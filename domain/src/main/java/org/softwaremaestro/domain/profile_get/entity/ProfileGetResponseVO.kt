package org.softwaremaestro.domain.profile_get.entity

data class ProfileGetResponseVO(
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
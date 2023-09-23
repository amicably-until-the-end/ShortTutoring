package org.softwaremaestro.domain.user_signup.entity

data class StudentSignupRequestVO(
    val vendor: String,
    val accessToken: String,
    val name: String,
    val bio: String,
    val schoolLevel: String,
    val schoolGrade: Long
)

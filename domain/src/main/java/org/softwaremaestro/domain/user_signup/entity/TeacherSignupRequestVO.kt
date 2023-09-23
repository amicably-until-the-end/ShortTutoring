package org.softwaremaestro.domain.user_signup.entity

data class TeacherSignupRequestVO(
    val vendor: String,
    val accessToken: String,
    val name: String,
    val bio: String,
    val schoolName: String,
    val schoolDivision: String,
    val schoolDepartment: String,
    val schoolGrade: Long
)
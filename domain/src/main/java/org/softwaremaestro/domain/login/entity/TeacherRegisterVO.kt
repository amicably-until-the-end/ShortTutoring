package org.softwaremaestro.domain.login.entity

data class TeacherRegisterVO(
    val bio: String,
    val name: String,
    val schoolName: String,
    val schoolDivision: String,
    val schoolDepartment: String,
    val schoolGrade: Int,
)
package org.softwaremaestro.domain.teacher_get.entity

data class TeacherVO(
    val profileUrl: String?,
    val nickname: String?,
    val teacherId: String?,
    val bio: String?,
    val pickCount: Int?,
    val univ: String?,
    val rating: Float?
)
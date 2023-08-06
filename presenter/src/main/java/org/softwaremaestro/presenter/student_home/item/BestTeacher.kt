package org.softwaremaestro.presenter.student_home.item

data class BestTeacher(
    val profileUrl: String,
    val nickname: String,
    val teacherId: String,
    val pickCount: Int,
    val univ: String,
    val rating: Float
)
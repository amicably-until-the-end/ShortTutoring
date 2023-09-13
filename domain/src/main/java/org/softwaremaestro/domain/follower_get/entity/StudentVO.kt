package org.softwaremaestro.domain.follower_get.entity

data class StudentVO(
    val profileUrl: String?,
    val nickname: String?,
    val grade: String?,
    val recentDate: String? = ""
)
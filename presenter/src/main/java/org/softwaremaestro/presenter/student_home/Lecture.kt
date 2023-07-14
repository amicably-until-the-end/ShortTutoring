package org.softwaremaestro.presenter.student_home

import javax.security.auth.Subject

data class Lecture(
    val description: String,
    val date: String,
    val subject: String,
    val chapter: String
)
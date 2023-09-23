package org.softwaremaestro.data.user_signup.model

import com.google.gson.annotations.SerializedName

data class TeacherSignupResultDto(
    @SerializedName("token") val token: String?
)

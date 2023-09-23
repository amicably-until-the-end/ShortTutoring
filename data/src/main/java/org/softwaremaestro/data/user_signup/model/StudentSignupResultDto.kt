package org.softwaremaestro.data.user_signup.model

import com.google.gson.annotations.SerializedName

data class StudentSignupResultDto(
    @SerializedName("token") val token: String?
)

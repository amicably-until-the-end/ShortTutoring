package org.softwaremaestro.data.login

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: Int
)

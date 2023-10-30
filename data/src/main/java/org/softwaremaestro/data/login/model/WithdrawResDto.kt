package org.softwaremaestro.data.login.model

import com.google.gson.annotations.SerializedName

data class WithdrawResDto(
    @SerializedName("vendor") val vendor: String?,
    @SerializedName("userId") val userId: String?,
)
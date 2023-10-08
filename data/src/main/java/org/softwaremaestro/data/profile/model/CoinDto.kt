package org.softwaremaestro.data.profile.model

import com.google.gson.annotations.SerializedName

data class CoinDto(
    @SerializedName("amount") val amount: Int?,
    @SerializedName("lastReceivedFreeCoinAt") val lastReceivedFreeCoinAt: String?
)

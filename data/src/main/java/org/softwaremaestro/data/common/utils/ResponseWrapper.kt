package org.softwaremaestro.data.common.utils

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T>(
    var code: Int,
    @SerializedName("message") var message: String?,
    @SerializedName("success") var error: Boolean?,
    @SerializedName("statusCode") var statusCode: Int?,
    @SerializedName("data") var data: List<T>? = null
)


data class WrappedResponse<T>(
    var code: Int,
    @SerializedName("message") var message: String?,
    @SerializedName("success") var error: Boolean?,
    @SerializedName("statusCode") var statusCode: Int?,
    @SerializedName("data") var data: T? = null
)
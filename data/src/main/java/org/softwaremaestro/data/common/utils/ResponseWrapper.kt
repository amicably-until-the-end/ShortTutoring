package org.softwaremaestro.data.common.utils

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T> (
    @SerializedName("message") var message : String,
    @SerializedName("statusCode") var statusCode : String,
    @SerializedName("error") var error : Boolean,
    @SerializedName("data") var data : List<T>? = null
)


data class WrappedResponse<T> (
    @SerializedName("message") var message : String,
    @SerializedName("statusCode") var statusCode : String,
    @SerializedName("error") var error : Boolean,
    @SerializedName("data") var data : T? = null
)
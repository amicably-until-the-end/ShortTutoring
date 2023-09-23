package org.softwaremaestro.presenter.login.univGet.model

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("schoolName") val schoolName: String
)
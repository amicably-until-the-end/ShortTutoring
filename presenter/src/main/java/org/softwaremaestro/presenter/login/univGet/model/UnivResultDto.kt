package org.softwaremaestro.presenter.login.univGet.model

import com.google.gson.annotations.SerializedName

data class UnivResultDto(
    @SerializedName("dataSearch") val dataSearch: DataSearch
)
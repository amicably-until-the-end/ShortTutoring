package org.softwaremaestro.presenter.login.univGet.model

import com.google.gson.annotations.SerializedName

data class DataSearch(
    @SerializedName("content") val content: List<Content>
)
package org.softwaremaestro.data.coin.remote

import org.softwaremaestro.data.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.GET

interface CoinApi {
    @GET("/user/receiveFreeCoin")
    suspend fun receiveFreeCoin(): Response<WrappedResponse<Boolean>>
}
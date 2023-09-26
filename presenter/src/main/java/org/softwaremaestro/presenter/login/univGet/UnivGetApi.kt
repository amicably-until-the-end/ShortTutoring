package org.softwaremaestro.presenter.login.univGet

import org.softwaremaestro.presenter.login.univGet.model.UnivResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnivGetApi {
    @GET("getOpenApi")
    suspend fun getUniv(
        @Query("apiKey") apiKey: String,
        @Query("svcType") svcType: String,
        @Query("svcCode") svcCode: String,
        @Query("contentType") contentType: String,
        @Query("gubun") gubun: String,
        @Query("sch1") sch1: Int,
        @Query("searchSchulNm") searchSchulNm: String
    ): Response<UnivResultDto>
}
package org.softwaremaestro.data.common.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.softwaremaestro.data.infra.SharedPrefs


data class TokenInfo(
    var vendor: String?,
    var token: String?
)

class RequestInterceptor constructor(private val prefs: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("retrofit", "intercept chain is ${chain.request().url.pathSegments[0]}")
        val jwt = getJWT(chain.request().url.pathSegments[0])

        var newRequest = if (jwt != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $jwt")
                .build()
        } else {
            chain.request()
        }

        Log.d("retrofit", "intercept request is $newRequest ")
        val response = chain.proceed(newRequest)
        Log.d(
            "retrofit",
            "intercept response is $response ${response.peekBody(Long.MAX_VALUE).string()}"
        )
        return response
    }

    private fun getJWT(role: String?): String? {
        return if (true)
            prefs.getJWT()
        else if (true) {
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsImF1dGhJZCI6bnVsbCwidXNlcklkIjoic3ktc3R1ZGVudC1pZCIsInJvbGUiOiJzdHVkZW50IiwiaWF0IjoxNjk1NjM0ODcwLCJleHAiOjE2OTU3MjEyNzB9.Xpo-unj27SwTnfnCUJIPc_BTeK5SPlS8LlDihjv6v6c"
        } else {
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsInVzZXJJZCI6InRlc3QtdGVhY2hlci1pZCIsInJvbGUiOiJ0ZWFjaGVyIiwiaWF0IjoxNjkxMTMyOTg3LCJleHAiOjE3MjI2OTA1ODd9.G1eeoixa12BW62OxRVZlgpSrWLzYVNgHPayyzJLJnaY"
        }
    }
}



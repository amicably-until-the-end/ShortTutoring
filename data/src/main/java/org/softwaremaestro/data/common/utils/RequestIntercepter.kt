package org.softwaremaestro.data.common.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
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
        return if (role.isNullOrEmpty())
            prefs.getJWT()
        // 학생
        else if (role == "student")
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsInVzZXJJZCI6InRlc3Qtc3R1ZGVudC1pZCIsInJvbGUiOiJzdHVkZW50IiwiaWF0IjoxNjkxMTMyODk3LCJleHAiOjE3MjI2OTA0OTd9.VwHlj3s8ZbruX4dQpPrvnTV93_LRVt_7YGMGP7emNuM"
        else
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsInVzZXJJZCI6InRlc3QtdGVhY2hlci1pZCIsInJvbGUiOiJ0ZWFjaGVyIiwiaWF0IjoxNjkxMTMyOTg3LCJleHAiOjE3MjI2OTA1ODd9.G1eeoixa12BW62OxRVZlgpSrWLzYVNgHPayyzJLJnaY"
    }
}



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
        val jwt = getJWT()

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

    private fun getJWT(): String? {
        // return prefs.getJWT()
        // 학생
        // return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsInVzZXJJZCI6InRlc3Qtc3R1ZGVudC1pZCIsInJvbGUiOiJzdHVkZW50IiwiaWF0IjoxNjkxMTMyODk3LCJleHAiOjE3MjI2OTA0OTd9.VwHlj3s8ZbruX4dQpPrvnTV93_LRVt_7YGMGP7emNuM"
        // 선생님
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsInVzZXJJZCI6InRlc3QtdGVhY2hlci1pZCIsInJvbGUiOiJ0ZWFjaGVyIiwiaWF0IjoxNjkxMTMyOTg3LCJleHAiOjE3MjI2OTA1ODd9.G1eeoixa12BW62OxRVZlgpSrWLzYVNgHPayyzJLJnaY"
    }
}



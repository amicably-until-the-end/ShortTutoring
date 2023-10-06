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

    private fun getJWT(role: String?): String {

        return prefs.getJWT()
    }
}



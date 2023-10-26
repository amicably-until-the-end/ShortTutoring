package org.softwaremaestro.data.common.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.softwaremaestro.data.infra.SharedPrefs
import java.lang.Integer.min


data class TokenInfo(
    var vendor: String?,
    var token: String?
)

class RequestInterceptor constructor(private val prefs: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val jwt = getJWT(chain.request().url.pathSegments[0])

        var newRequest = if (jwt != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $jwt")
                .build()
        } else {
            chain.request()
        }


        val request = newRequest.toString()
        Log.d(
            "retrofit",
            "intercept request is ${request.slice(0..min(request.length - 1, 1000))} "
        )
        val response = chain.proceed(newRequest)
        val responseString = response.peekBody(Long.MAX_VALUE).string()
        Log.d(
            "retrofit",
            "intercept response is $response ${
                responseString.slice(
                    0..min(
                        responseString.length - 1,
                        1000
                    )
                )
            }"
        )
        return response
    }

    private fun getJWT(role: String?): String {
//        prefs.clearJWT()
//        prefs.saveJWT("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsImF1dGhJZCI6bnVsbCwidXNlcklkIjoiaGMtc3R1ZGVudC1pZCIsInJvbGUiOiJzdHVkZW50IiwiaWF0IjoxNjk1NjM0ODAwLCJleHAiOjE2OTU3MjEyMDB9.MoFrAW4cGuKhUhBFygk3rAlR4jhEgGSrCDQZ4oQ2uN4")
//        prefs.saveJWT("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsImF1dGhJZCI6bnVsbCwidXNlcklkIjoiaGMtdGVhY2hlci1pZCIsInJvbGUiOiJ0ZWFjaGVyIiwiaWF0IjoxNjk1NjM0NTgzLCJleHAiOjE2OTU3MjA5ODN9.EvDgOXH7V3A-QB3lT2BSbTrmdPeek4Rhc6WM5iVQ_5k")
        return prefs.getJWT()
    }
}



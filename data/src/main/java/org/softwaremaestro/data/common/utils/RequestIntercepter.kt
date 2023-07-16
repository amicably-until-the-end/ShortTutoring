package org.softwaremaestro.data.common.utils

import okhttp3.Interceptor
import okhttp3.Response
import org.softwaremaestro.data.infra.SharedPrefs

class RequestInterceptor constructor(private val prefs: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefs.getToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("user_token", token)
            .build()
        return chain.proceed(newRequest)
    }
}
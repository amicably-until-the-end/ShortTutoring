package org.softwaremaestro.data.common.utils

import android.util.Log
import com.auth0.android.jwt.JWT
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.softwaremaestro.data.infra.SharedPrefs
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


data class TokenInfo(
    var vendor: String?,
    var token: String?
)

class RequestInterceptor constructor(private val prefs: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val jwt = getJWT()

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $jwt")
            .build()
        Log.d("retrofit", "intercept request is ${newRequest.toString()}")
        return chain.proceed(newRequest)
    }

    private fun getJWT(): String {
        return ""
    }
}



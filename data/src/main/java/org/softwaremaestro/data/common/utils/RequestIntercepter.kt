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
        val tokenInfo = getTokenInfo()

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${tokenInfo.token}")
            .addHeader("vendor", tokenInfo.vendor ?: "")
            .build()
        Log.d("retrofit", "intercept request is ${newRequest.toString()}")
        return chain.proceed(newRequest)
    }

    private suspend fun getKakaoToken(): OAuthToken? {
        if (AuthApiClient.instance.hasToken()) {
            return suspendCoroutine { continuation ->
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Log.d("mymymy", "kakao error ${error}")
                        if (error is KakaoSdkError && error.isInvalidTokenError()) {
                            // 로그인 필요
                            continuation.resume(null)
                        } else {
                            // 기타 에러
                            continuation.resume(null)
                        }
                    } else {
                        val token = TokenManager.instance.getToken()
                        Log.d("mymymy", "kakao success ${token}")
                        if (token != null) {
                            continuation.resume(token)
                        } else {
                            // 토큰이 null일 경우 처리
                            continuation.resume(null)
                        }
                    }
                }
            }
        } else {
            // 로그인 필요
            return null
        }
    }


    private fun getTokenInfo(): TokenInfo {

        return TokenInfo(null, null)

        /*val kakaoToken = runBlocking { getKakaoToken() }
        if (kakaoToken?.accessToken != null) {
            return TokenInfo("kakao", kakaoToken.accessToken!!)
        }
        return TokenInfo(null, null)*/
    }
}



package org.softwaremaestro.data.common.utils

import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.softwaremaestro.data.infra.SharedPrefs
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RequestInterceptor constructor(private val prefs: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { getToken() }

        //저장된 유효한 JWT를 가져오는데 실패하면 user_token을 비워서 보냄

        val newRequest = chain.request().newBuilder()
            .addHeader("user_token", token ?: "")
            .build()
        return chain.proceed(newRequest)
    }

    private suspend fun getKakaoToken(): OAuthToken? {
        if (AuthApiClient.instance.hasToken()) {
            return suspendCoroutine { continuation ->
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError()) {
                            // 로그인 필요
                            continuation.resume(null)
                        } else {
                            // 기타 에러
                            continuation.resume(null)
                        }
                    } else {
                        val token = TokenManager.instance.getToken()
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


    private suspend fun getToken(): String? {
        val kakaoToken = getKakaoToken()
        if (kakaoToken?.idToken != null) {
            return kakaoToken.idToken!!
        }
        return null
    }

}

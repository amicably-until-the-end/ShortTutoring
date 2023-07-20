package org.softwaremaestro.data.common.utils

import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import okhttp3.Interceptor
import okhttp3.Response
import org.softwaremaestro.data.infra.SharedPrefs

class RequestInterceptor constructor(private val prefs: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getToken()

        val newRequest = chain.request().newBuilder()
            .addHeader("user_token", token)
            .build()
        return chain.proceed(newRequest)
    }

    private fun getKakaoToken(): OAuthToken {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                    } else {
                        //기타 에러
                    }
                } else {
                    val token = TokenManager.instance.getToken()
                    if (token != null) {
                        return TokenManager.instance.getToken()!!
                    }
                }
            }
        } else {
            //로그인 필요
        }
    }

    private fun getToken(): String {
        val kakaoToken = getKakaoToken()
        if (kakaoToken?.idToken != null) return kakaoToken.idToken!!
    }
}

package org.softwaremaestro.data.common.utils

import android.util.Log
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.TokenManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SavedToken {

    fun getKakaoToken(): OAuthToken? {
        return TokenManager.instance.getToken()
    }


    fun getTokenInfo(): TokenInfo {


        val kakaoToken = runBlocking { getKakaoToken() }
        if (kakaoToken?.accessToken != null) {
            return TokenInfo("kakao", kakaoToken.accessToken!!)
        }

        return TokenInfo(null, null)
    }
}
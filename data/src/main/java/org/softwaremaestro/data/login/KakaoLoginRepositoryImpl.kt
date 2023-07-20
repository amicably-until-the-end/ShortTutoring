package org.softwaremaestro.data.login

import android.content.Context
import android.util.Log
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginRepositoryImpl : KakaoLoginRepository {
    /*
    override suspend fun loginWithKakao(): Flow<BaseResult<String, String>> {
        return callbackFlow {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        Log.d("mymymy", "kakao app error ${error}")
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            Log.d("mymymy", "clientError")
                            return@loginWithKakaoTalk
                        }
                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                            if (error != null) {
                                trySend(BaseResult.Error("kakao login fail"))
                            } else {
                                trySend(BaseResult.Success("kakao login succeess with kakao"))
                                Log.i("mymymy", "카카오톡으로 로그인 성공 ${token?.idToken}")
                            }
                        }
                    } else if (token != null) {
                        trySend(BaseResult.Success("kakao login success with kakaotalk"))
                        Log.i("mymymy", "카카오톡으로 로그인 성공 ${token.idToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        trySend(BaseResult.Error("kakao login fail"))
                    } else {
                        trySend(BaseResult.Success(token?.idToken!!))
                        Log.i("mymymy", "카카오톡으로 로그인 성공 ${token.idToken}")
                    }
                }
            }
            awaitClose {
                Log.d("mymymy", "nothing")
                trySend(BaseResult.Error("await close"))
            }
        }
    }

     */
    override suspend fun loginWithKakao(): Flow<BaseResult<String, String>> {
        return flow {
            BaseResult.Success("asdf")
        }
    }
}
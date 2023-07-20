package org.softwaremaestro.data.login

import android.content.Context
import android.util.Log
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.infra.SharedPrefs
import org.softwaremaestro.data.login.model.UserInfoReqDto
import org.softwaremaestro.data.login.remote.GetUserInfoApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.entity.UserVO
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val getUserInfoApi: GetUserInfoApi,
    private val prefs: SharedPrefs,
    private val context: Context
) :
    LoginRepository {


    override suspend fun autoLogin(): Flow<BaseResult<String, String>> {
        return flow {
            val savedToken: String = prefs.getToken()
            //token 만료되었으면 리프래시 하는 로직 추가.
            if (savedToken == "") {
                emit(BaseResult.Error("No saved token"))
                prefs.saveToken("userToken")
            } else {
                // 토큰 유효한지 체크
                // 유효하면 return
                // 만료되었으면 리프레시하고 return
                // 실패하면 return Error
                emit(BaseResult.Success(savedToken!!))
            }
        }
    }

    override suspend fun getUserInfo(): Flow<BaseResult<UserVO, String>> {
        return flow {


            val result = getUserInfoApi.getUserInfo()
            if (result.isSuccessful) {
                val userDto = result.body()?.data!!
                emit(BaseResult.Success(UserVO(userDto.role, null, userDto.name)))
            } else {
                emit(BaseResult.Error("Fail to get user info"))
            }
        }
    }

    override suspend fun loginWithKakao(): Flow<BaseResult<String, String>> {
        return callbackFlow {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                            if (error != null) {
                                trySend(BaseResult.Error("kakao login fail"))
                            } else {
                                trySend(BaseResult.Success("kakao login succeess with kakao"))
                                Log.i("kakao", "카카오톡으로 로그인 성공 ${token?.idToken}")
                            }
                        }
                    } else if (token != null) {
                        trySend(BaseResult.Success("kakao login success with kakaotalk"))
                        Log.i("kakao", "카카오톡으로 로그인 성공 ${token.idToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        trySend(BaseResult.Error("kakao login fail"))
                    } else {
                        trySend(BaseResult.Success(token?.idToken!!))
                        Log.i("kakao", "카카오톡으로 로그인 성공 ${token.idToken}")
                    }
                }
            }
        }
    }
}
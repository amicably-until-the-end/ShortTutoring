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
    private val prefs: SharedPrefs
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

    override fun saveKakaoJWT(token: String) {
        prefs.saveToken(token)
    }

}
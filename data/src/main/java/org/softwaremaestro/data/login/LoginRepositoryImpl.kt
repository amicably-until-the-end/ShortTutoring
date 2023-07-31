package org.softwaremaestro.data.login

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.infra.SharedPrefs
import org.softwaremaestro.data.common.module.SavedTokenModule
import org.softwaremaestro.data.common.utils.SavedToken
import org.softwaremaestro.data.login.model.LoginReqDto
import org.softwaremaestro.data.login.remote.LoginApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.entity.UserVO
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    private val prefs: SharedPrefs,
    private val savedToken: SavedToken,
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

    override suspend fun login(): Flow<BaseResult<UserVO, String>> {
        Log.d("login", "login")
        return flow {

            Log.d("login", "Saved Token :${"savedToken.getTokenInfo()?.token"}")
            val result =
                loginApi.login(
                    LoginReqDto(
                        savedToken.getTokenInfo().vendor!!,
                        savedToken.getTokenInfo().token!!
                    )
                )
            Log.d("login", result.toString())
            if (result.isSuccessful) {
                val userDto = result.body()?.data!!
                //TODO: JWT 저장
                emit(BaseResult.Success(UserVO(userDto.role, null, userDto.name)))
            } else {
                emit(BaseResult.Error("Fail to login"))
            }
        }
    }

    override fun saveKakaoJWT(token: String) {
        prefs.saveToken(token)
    }


}
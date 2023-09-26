package org.softwaremaestro.data.login

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.infra.SharedPrefs
import org.softwaremaestro.data.common.module.SavedTokenModule
import org.softwaremaestro.data.common.utils.SavedToken
import org.softwaremaestro.data.login.model.LoginReqDto
import org.softwaremaestro.data.login.model.UserInfoResDto
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
            val savedToken: String = prefs.getJWT()
            //token 만료되었으면 리프래시 하는 로직 추가.
            if (savedToken == "") {
                emit(BaseResult.Error("No saved token"))
                prefs.getJWT()
            } else {
                // 토큰 유효한지 체크
                // 유효하면 return
                // 만료되었으면 리프레시하고 return
                // 실패하면 return Error
                emit(BaseResult.Success(savedToken!!))
            }
        }
    }

    override suspend fun login(): Flow<BaseResult<String, String>> {
        return flow {

            val result =
                loginApi.login(
                    LoginReqDto(
                        savedToken.getTokenInfo().vendor!!,
                        savedToken.getTokenInfo().token!!
                    )
                )
            if (result.isSuccessful && result.body()?.success == true) {
                val loginData = result.body()?.data!!
                prefs.saveJWT(loginData.JWT)
                emit(
                    BaseResult.Success(
                        loginData.role
                    )
                )
            } else {
                emit(BaseResult.Error("Fail to login"))
            }
        }
    }

    override fun saveKakaoJWT(token: String) {
        //
    }

    override fun getUserInfo(): Flow<BaseResult<UserVO, String>> {
        return flow {
            val result = loginApi.getUserInfo()
            if (result.isSuccessful) {
                val userInfo = result.body()?.data!!
                emit(
                    BaseResult.Success(
                        UserVO(
                            userInfo.role,
                            userInfo.id,
                            userInfo.bio,
                            userInfo.name,
                            userInfo.profileImage
                        )
                    )
                )
            } else {
                emit(BaseResult.Error("Fail to get user info"))
            }
        }
    }

    override fun getToken(): String {
        return prefs.getJWT()
    }


}
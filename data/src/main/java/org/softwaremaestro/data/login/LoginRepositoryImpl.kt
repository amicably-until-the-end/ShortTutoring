package org.softwaremaestro.data.login

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.infra.SharedPrefs
import org.softwaremaestro.data.login.model.UserInfoReqDto
import org.softwaremaestro.data.login.remote.GetUserInfoApi
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.entity.UserVO
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val getUserInfoApi: GetUserInfoApi
) :
    LoginRepository {


    override suspend fun autoLogin(): Flow<BaseResult<String, String>> {
        return flow {
            val savedToken: String = ""//prefs.getToken()
            //token 만료되었으면 리프래시 하는 로직 추가.
            if (savedToken == "") {
                emit(BaseResult.Error("Fail to get token"))
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
            val savedToken: String = ""//prefs.getToken()
            val result = getUserInfoApi.getUserInfo(UserInfoReqDto(savedToken))
            if (result.isSuccessful) {
                val userDto = result.body()?.data!!
                emit(BaseResult.Success(UserVO(userDto.role, null, userDto.name)))
            } else {
                emit(BaseResult.Error("Fail to get user info"))
            }
        }
    }


}
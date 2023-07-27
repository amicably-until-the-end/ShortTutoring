package org.softwaremaestro.domain.login

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.UserVO

interface LoginRepository {
    suspend fun autoLogin(): Flow<BaseResult<String, String>>

    suspend fun getUserInfo(): Flow<BaseResult<UserVO, String>>

    fun saveKakaoJWT(token: String)

}
package org.softwaremaestro.domain.login

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.UserVO

interface KakaoLoginRepository {

    suspend fun loginWithKakao(): Flow<BaseResult<String, String>>
}
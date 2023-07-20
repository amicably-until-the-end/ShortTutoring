package org.softwaremaestro.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.KakaoLoginRepository
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.entity.UserVO
import javax.inject.Inject

class LoginWithKakaoUseCase @Inject constructor(private val repository: KakaoLoginRepository) {
    suspend fun execute(): Flow<BaseResult<String, String>> = repository.loginWithKakao()
}
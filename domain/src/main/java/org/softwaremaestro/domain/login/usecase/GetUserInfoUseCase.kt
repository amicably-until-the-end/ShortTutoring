package org.softwaremaestro.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.entity.UserVO
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun execute(): Flow<BaseResult<UserVO, String>> = repository.getUserInfo()
}
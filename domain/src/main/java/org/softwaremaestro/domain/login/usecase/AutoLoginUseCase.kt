package org.softwaremaestro.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import javax.inject.Inject


class AutoLoginUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun execute(): Flow<BaseResult<String, String>> = repository.autoLogin()
}
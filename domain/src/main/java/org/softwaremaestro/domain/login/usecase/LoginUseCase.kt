package org.softwaremaestro.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun execute(): Flow<BaseResult<String, String>> = repository.login()

    suspend fun registerFCMToken(): Flow<BaseResult<String, String>> = repository.registerFCMToken()

    fun clearJWT(): String = repository.clearJWT()

}
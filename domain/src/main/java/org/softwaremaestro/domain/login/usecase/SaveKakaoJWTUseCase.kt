package org.softwaremaestro.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import javax.inject.Inject


class SaveKakaoJWTUseCase @Inject constructor(private val repository: LoginRepository) {
    fun save(token: String) = repository.saveKakaoJWT(token)
}
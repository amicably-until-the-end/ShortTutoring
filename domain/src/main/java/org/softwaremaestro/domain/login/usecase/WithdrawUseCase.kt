package org.softwaremaestro.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.RegisterRepository
import org.softwaremaestro.domain.login.entity.WithdrawResVO
import javax.inject.Inject


class WithdrawUseCase @Inject constructor(private val repository: RegisterRepository) {
    suspend fun execute(): Flow<BaseResult<WithdrawResVO, String>> =
        repository.withdraw()
}
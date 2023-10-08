package org.softwaremaestro.domain.coin.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.coin.CoinRepository
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class CoinFreeReceiveUseCase @Inject constructor(private val coinRepository: CoinRepository) {

    suspend fun execute(): Flow<BaseResult<String, String>> {
        return coinRepository.receiveFreeCoin()
    }
}
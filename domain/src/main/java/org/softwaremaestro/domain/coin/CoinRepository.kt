package org.softwaremaestro.domain.coin

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.common.BaseResult

interface CoinRepository {

    suspend fun receiveFreeCoin(): Flow<BaseResult<String, String>>
}
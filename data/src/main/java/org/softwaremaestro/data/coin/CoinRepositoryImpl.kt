package org.softwaremaestro.data.coin

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.coin.remote.CoinApi
import org.softwaremaestro.domain.coin.CoinRepository
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(private val coinApi: CoinApi) :
    CoinRepository {

    override suspend fun receiveFreeCoin(): Flow<BaseResult<String, String>> {
        return flow {
            val response = coinApi.receiveFreeCoin()
            val body = response.body()
            if (body?.success == true) {
                body.data?.let { emit(BaseResult.Success("오늘의 코인을 얻었습니다.")) }
            } else {
                val errorString = "error in ${this@CoinRepositoryImpl::class.java.name}\n" +
                        "message : ${body?.message}"
                emit(BaseResult.Error(errorString))
            }
        }
    }
}
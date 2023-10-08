package org.softwaremaestro.data.coin

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.coin.remote.CoinApi
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.domain.coin.CoinRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class CoinModule {

    @Singleton
    @Provides
    fun provideCoinApi(retrofit: Retrofit): CoinApi {
        return retrofit.create(CoinApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCoinRepository(coinApi: CoinApi): CoinRepository {
        return CoinRepositoryImpl(coinApi)
    }
}
package org.softwaremaestro.data.offer_remove

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.offer_remove.remote.OfferRemoveApi
import org.softwaremaestro.domain.offer_remove.OfferRemoveRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class OfferRemoveModule {

    @Singleton
    @Provides
    fun provideOfferRemoveApi(retrofit: Retrofit): OfferRemoveApi {
        return retrofit.create(OfferRemoveApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOfferRemoveRepository(offerRemoveApi: OfferRemoveApi): OfferRemoveRepository {
        return OfferRemoveRepositoryImpl(offerRemoveApi)
    }
}
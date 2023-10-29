package org.softwaremaestro.data.schedule_offer

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.schedule_offer.remote.ScheduleOfferApi
import org.softwaremaestro.domain.schedule_offer.ScheduleOfferRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ScheduleOfferModule {

    @Singleton
    @Provides
    fun provideScheduleOfferApi(retrofit: Retrofit): ScheduleOfferApi {
        return retrofit.create(ScheduleOfferApi::class.java)
    }

    @Singleton
    @Provides
    fun provideScheduleOfferRepository(scheduleOfferApi: ScheduleOfferApi): ScheduleOfferRepository {
        return ScheduleOfferRepositoryImpl(scheduleOfferApi)
    }
}
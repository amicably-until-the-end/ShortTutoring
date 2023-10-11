package org.softwaremaestro.data.event

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.event.remote.EventApi
import org.softwaremaestro.domain.event.entity.EventRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class EventModule {

    @Singleton
    @Provides
    fun provideEventApi(retrofit: Retrofit): EventApi {
        return retrofit.create(EventApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEventRepository(eventApi: EventApi): EventRepository {
        return EventRepositoryImpl(eventApi)
    }
}
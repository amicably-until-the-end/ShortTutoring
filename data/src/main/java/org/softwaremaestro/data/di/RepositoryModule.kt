package org.softwaremaestro.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.repository.RequestRepositoryImpl
import org.softwaremaestro.domain.repository.RequestRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsRequestRepository(
        requestRepositoryImpl: RequestRepositoryImpl
    ): RequestRepository
}
package org.softwaremaestro.data.follow

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.follow.remote.FollowApi
import org.softwaremaestro.domain.follow.FollowRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class UserFollowModule {

    @Singleton
    @Provides
    fun provideUserFollowApi(retrofit: Retrofit): FollowApi {
        return retrofit.create(FollowApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserFollowRepository(followApi: FollowApi): FollowRepository {
        return FollowRepositoryImpl(followApi)
    }
}
package org.softwaremaestro.data.follower_get

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.follower_get.remote.FollowerGetApi
import org.softwaremaestro.domain.follower_get.FollowerGetRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class FollowerGetModule {

    @Singleton
    @Provides
    fun provideFollowerGetApi(retrofit: Retrofit): FollowerGetApi {
        return retrofit.create(FollowerGetApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFollowerGetRepository(followerGetApi: FollowerGetApi): FollowerGetRepository {
        return FollowerGetRepositoryImpl(followerGetApi)
    }
}
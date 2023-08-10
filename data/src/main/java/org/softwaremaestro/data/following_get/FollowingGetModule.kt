package org.softwaremaestro.data.following_get

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.following_get.remote.FollowingGetApi
import org.softwaremaestro.domain.following_get.FollowingGetRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class FollowingGetModule {

    @Singleton
    @Provides
    fun provideFollowingGetApi(retrofit: Retrofit): FollowingGetApi {
        return retrofit.create(FollowingGetApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFollowingGetRepository(followingGetApi: FollowingGetApi): FollowingGetRepository {
        return FollowingGetRepositoryImpl(followingGetApi)
    }
}
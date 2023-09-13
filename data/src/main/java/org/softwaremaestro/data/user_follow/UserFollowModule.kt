package org.softwaremaestro.data.user_follow

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.user_follow.remote.UserFollowApi
import org.softwaremaestro.domain.user_follow.UserFollowRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class UserFollowModule {

    @Singleton
    @Provides
    fun provideUserFollowApi(retrofit: Retrofit): UserFollowApi {
        return retrofit.create(UserFollowApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserFollowRepository(userFollowApi: UserFollowApi): UserFollowRepository {
        return UserFollowRepositoryImpl(userFollowApi)
    }
}
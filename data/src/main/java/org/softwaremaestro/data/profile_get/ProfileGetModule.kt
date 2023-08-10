package org.softwaremaestro.data.profile_get

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.profile_get.remote.ProfileGetApi
import org.softwaremaestro.domain.profile_get.ProfileGetRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ProfileGetModule {

    @Singleton
    @Provides
    fun provideProfileGetApi(retrofit: Retrofit): ProfileGetApi {
        return retrofit.create(ProfileGetApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileGetRepository(profileGetApi: ProfileGetApi): ProfileGetRepository {
        return ProfileGetRepositoryImpl(profileGetApi)
    }
}
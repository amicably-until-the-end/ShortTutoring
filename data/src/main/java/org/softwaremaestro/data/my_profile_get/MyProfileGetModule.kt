package org.softwaremaestro.data.my_profile_get

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.my_profile_get.remote.MyProfileGetApi
import org.softwaremaestro.domain.my_profile_get.MyProfileGetRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class MyProfileGetModule {

    @Singleton
    @Provides
    fun provideMyProfileGetApi(retrofit: Retrofit): MyProfileGetApi {
        return retrofit.create(MyProfileGetApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMyProfileGetRepository(myProfileGetApi: MyProfileGetApi): MyProfileGetRepository {
        return MyProfileGetRepositoryImpl(myProfileGetApi)
    }
}
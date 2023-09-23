package org.softwaremaestro.data.user_signup

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.user_signup.remote.UserSignupApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class UserSignupModule {

    @Singleton
    @Provides
    fun provideUserSignupApi(retrofit: Retrofit): UserSignupApi {
        return retrofit.create(UserSignupApi::class.java)
    }
}
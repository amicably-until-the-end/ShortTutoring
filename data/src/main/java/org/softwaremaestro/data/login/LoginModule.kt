package org.softwaremaestro.data.login

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.common.utils.SavedToken
import org.softwaremaestro.data.infra.SharedPrefs
import org.softwaremaestro.data.login.remote.FCMApi
import org.softwaremaestro.data.login.remote.LoginApi
import org.softwaremaestro.data.login.remote.RegisterApi
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.RegisterRepository
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Provides
    fun provideGetUserInfoApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    fun provideLoginRepository(
        sharedPrefs: SharedPrefs,
        loginApi: LoginApi,
        savedToken: SavedToken,
        fcmApi: FCMApi
    ): LoginRepository {
        return LoginRepositoryImpl(loginApi, fcmApi, sharedPrefs, savedToken)
    }

    @Provides
    fun provideRegisterApi(retrofit: Retrofit): RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    @Provides
    fun provideFCMApi(retrofit: Retrofit): FCMApi {
        return retrofit.create(FCMApi::class.java)
    }

    @Provides
    fun provideRegisterRepository(
        registerApi: RegisterApi,
        savedToken: SavedToken
    ): RegisterRepository {
        return RegisterRepositoryImpl(registerApi, savedToken)
    }

}
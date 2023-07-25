package org.softwaremaestro.data.login

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.answer_upload.remote.AnswerUploadApi
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.infra.SharedPrefs
import org.softwaremaestro.data.login.remote.GetUserInfoApi
import org.softwaremaestro.data.login.remote.RegisterApi
import org.softwaremaestro.data.question_get.QuestionGetRepositoryImpl
import org.softwaremaestro.data.question_get.remote.QuestionGetApi
import org.softwaremaestro.domain.answer_upload.AnswerUploadRepository
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.login.RegisterRepository
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Provides
    fun provideGetUserInfoApi(retrofit: Retrofit): GetUserInfoApi {
        return retrofit.create(GetUserInfoApi::class.java)
    }

    @Provides
    fun provideAnswerUploadRepository(
        sharedPrefs: SharedPrefs,
        getUserInfoApi: GetUserInfoApi
    ): LoginRepository {
        return LoginRepositoryImpl(getUserInfoApi, sharedPrefs)
    }

    @Provides
    fun provideRegisterApi(retrofit: Retrofit): RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    @Provides
    fun provideRegisterRepository(
        registerApi: RegisterApi
    ): RegisterRepository {
        return RegisterRepositoryImpl(registerApi)
    }
}
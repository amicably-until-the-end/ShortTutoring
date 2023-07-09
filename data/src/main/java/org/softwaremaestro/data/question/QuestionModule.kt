package org.softwaremaestro.data.question

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.question.remote.QuestionApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class QuestionModule {

    @Singleton
    @Provides
    fun provideQuestionsApi(retrofit: Retrofit): QuestionApi{
        return retrofit.create(QuestionApi::class.java)
    }

}
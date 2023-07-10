package org.softwaremaestro.data.question_get

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.question_get.remote.QuestionGetApi
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class QuestionGetModule {

    @Singleton
    @Provides
    fun provideQuestionGetApi(retrofit: Retrofit): QuestionGetApi {
        return retrofit.create(QuestionGetApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuestionGetRepository(questionGetApi: QuestionGetApi): QuestionGetRepository {
        return QuestionGetRepositoryImpl(questionGetApi)
    }

}
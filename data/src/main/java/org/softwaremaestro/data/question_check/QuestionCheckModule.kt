package org.softwaremaestro.data.question_check

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.question_check.remote.QuestionCheckApi
import org.softwaremaestro.data.question_upload.QuestionUploadRepositoryImpl
import org.softwaremaestro.data.question_upload.remote.QuestionUploadApi
import org.softwaremaestro.domain.question_check.QuestionCheckRepository
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class QuestionCheckModule {

    @Singleton
    @Provides
    fun provideQuestionCheckApi(retrofit: Retrofit): QuestionCheckApi {
        return retrofit.create(QuestionCheckApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuestionCheckRepository(questionCheckApi: QuestionCheckApi): QuestionCheckRepository {
        return QuestionCheckRepositoryImpl(questionCheckApi)
    }
}
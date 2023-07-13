package org.softwaremaestro.data.question_upload

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.question_upload.remote.QuestionUploadApi
import org.softwaremaestro.domain.question_upload.QuestionUploadRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class QuestionUploadModule {

    @Singleton
    @Provides
    fun provideQuestionUploadApi(retrofit: Retrofit): QuestionUploadApi {
        return retrofit.create(QuestionUploadApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuestionUploadRepository(questionUploadApi: QuestionUploadApi): QuestionUploadRepository {
        return QuestionUploadRepositoryImpl(questionUploadApi)
    }
}
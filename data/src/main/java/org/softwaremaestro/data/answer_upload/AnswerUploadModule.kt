package org.softwaremaestro.data.answer_upload

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.answer_upload.remote.AnswerUploadApi
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.question_get.QuestionGetRepositoryImpl
import org.softwaremaestro.data.question_get.remote.QuestionGetApi
import org.softwaremaestro.domain.answer_upload.AnswerUploadRepository
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class AnswerUploadModule {

    @Singleton
    @Provides
    fun provideAnswerUploadApi(retrofit: Retrofit): AnswerUploadApi {
        return retrofit.create(AnswerUploadApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAnswerUploadRepository(answerUploadApi: AnswerUploadApi): AnswerUploadRepository {
        return AnswerUploadRepositoryImpl(answerUploadApi)
    }
}
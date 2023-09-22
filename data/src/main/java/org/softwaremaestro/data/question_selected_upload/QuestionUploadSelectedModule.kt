package org.softwaremaestro.data.question_selected_upload

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.question_selected_upload.remote.QuestionSelectedUploadApi
import org.softwaremaestro.domain.question_selected_upload.QuestionSelectedUploadRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class QuestionUploadSelectedModule {

    @Singleton
    @Provides
    fun provideQuestionSelectedUploadApi(retrofit: Retrofit): QuestionSelectedUploadApi {
        return retrofit.create(QuestionSelectedUploadApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuestionSelctedUploadRepository(questionSelectedUploadApi: QuestionSelectedUploadApi): QuestionSelectedUploadRepository {
        return QuestionSelectedUploadRepositoryImpl(questionSelectedUploadApi)
    }
}
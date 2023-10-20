package org.softwaremaestro.data.tutoring_get

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.question_get.remote.QuestionGetApi
import org.softwaremaestro.data.tutoring_get.remote.TutoringGetApi
import org.softwaremaestro.domain.question_get.QuestionGetRepository
import org.softwaremaestro.domain.tutoring_get.TutoringRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class TutoringGetModule {

    @Singleton
    @Provides
    fun provideTutoringGetApi(retrofit: Retrofit): TutoringGetApi {
        return retrofit.create(TutoringGetApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTutoringGetRepository(tutoringGetApi: TutoringGetApi): TutoringRepository {
        return TutoringRepositoryImpl(tutoringGetApi)
    }
}
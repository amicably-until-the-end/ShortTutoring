package org.softwaremaestro.data.best_teacher_get

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.best_teacher_get.remote.BestTeacherGetApi
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.domain.best_teacher_get.BestTeacherRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class BestTeacherGetModule {
    @Singleton
    @Provides
    fun provideBestTeacherGetApi(retrofit: Retrofit): BestTeacherGetApi {
        return retrofit.create(BestTeacherGetApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBestTeacherRepository(bestTeacherGetApi: BestTeacherGetApi): BestTeacherRepository {
        return BestTeacherRepositoryImpl(bestTeacherGetApi)
    }
}
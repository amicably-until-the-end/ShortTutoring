package org.softwaremaestro.data.teacher_onlines_get

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.teacher_onlines_get.remote.TeacherOnlinesGetApi
import org.softwaremaestro.domain.teacher_onlines_get.TeacherOnlinesGetRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class TeacherOnlinesGetModule {

    @Singleton
    @Provides
    fun provideTeacherOnlinesGetApi(retrofit: Retrofit): TeacherOnlinesGetApi {
        return retrofit.create(TeacherOnlinesGetApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTeacherOnlinesGetRepository(teacherOnlinesGetApi: TeacherOnlinesGetApi): TeacherOnlinesGetRepository {
        return TeacherOnlinesGetRepositoryImpl(teacherOnlinesGetApi)
    }
}
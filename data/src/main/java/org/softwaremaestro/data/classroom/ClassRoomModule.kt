package org.softwaremaestro.data.classroom

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.classroom.remote.ClassRoomApi
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.domain.classroom.ClassRoomRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ClassRoomModule {

    @Singleton
    @Provides
    fun provideClassRoomApi(retrofit: Retrofit): ClassRoomApi {
        return retrofit.create(ClassRoomApi::class.java)
    }

    @Singleton
    @Provides
    fun provideClassRoomRepository(classRoomApi: ClassRoomApi): ClassRoomRepository {
        return ClassRoomRepositoryImpl(classRoomApi)
    }
}
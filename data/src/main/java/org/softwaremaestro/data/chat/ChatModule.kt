package org.softwaremaestro.data.chat

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.chat.remote.ChatApi
import org.softwaremaestro.data.classroom.remote.ClassRoomApi
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.my_profile_get.MyProfileGetRepositoryImpl
import org.softwaremaestro.data.my_profile_get.remote.MyProfileGetApi
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.classroom.ClassRoomRepository
import org.softwaremaestro.domain.my_profile_get.MyProfileGetRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ChatModule {

    @Singleton
    @Provides
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Singleton
    @Provides
    fun provideChatRepository(chatApi: ChatApi): ChatRepository {
        return ChatRepositoryImpl(chatApi)
    }
}
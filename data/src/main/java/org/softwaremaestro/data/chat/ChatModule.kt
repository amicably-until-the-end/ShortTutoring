package org.softwaremaestro.data.chat

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.chat.database.ChatDatabase
import org.softwaremaestro.data.chat.remote.ChatApi
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.question_get.remote.QuestionGetApi
import org.softwaremaestro.domain.chat.ChatRepository
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
    fun provideChatRepository(
        chatApi: ChatApi,
        chatDatabase: ChatDatabase,
        questionGetApi: QuestionGetApi
    ): ChatRepository {
        return ChatRepositoryImpl(chatApi, chatDatabase, questionGetApi)
    }

    @Singleton
    @Provides
    fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase {
        return ChatDatabase.getInstance(context)!!
    }
}
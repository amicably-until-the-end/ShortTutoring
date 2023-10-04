package org.softwaremaestro.domain.chat

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.common.BaseResult

interface ChatRepository {
    suspend fun getRoomList(
        isTeacher: Boolean,
        currentRoomId: String?
    ): Flow<BaseResult<ChatRoomListVO, String>>

    suspend fun insertMessage(
        roomId: String,
        body: String,
        format: String,
        sendAt: String,
        isMyMsg: Boolean
    )

    suspend fun getChatRoomInfo(chatRoomId: String): Flow<BaseResult<ChatRoomVO, String>>

    suspend fun getMessages(chatRoomId: String): Flow<BaseResult<List<MessageVO>, String>>

    suspend fun markAsRead(chatRoomId: String)
}
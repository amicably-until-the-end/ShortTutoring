package org.softwaremaestro.domain.chat.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class GetChatRoomListUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend fun execute(
        isTeacher: Boolean,
        currentRoomId: String?
    ): Flow<BaseResult<ChatRoomListVO, String>> =
        repository.getRoomList(isTeacher, currentRoomId)

    suspend fun getChatRoom(
        chatRoomId: String
    ): Flow<BaseResult<ChatRoomVO, String>> =
        repository.getChatRoomInfo(chatRoomId)

    suspend fun markAsRead(chatRoomId: String) = repository.markAsRead(chatRoomId)
}
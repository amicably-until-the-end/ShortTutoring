package org.softwaremaestro.domain.chat.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class GetChatRoomListUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend fun execute(isTeacher: Boolean): Flow<BaseResult<ChatRoomListVO, String>> =
        repository.getRoomList(isTeacher)
}
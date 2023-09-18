package org.softwaremaestro.domain.chat.usecase

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class GetChatRoomListUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend fun execute(
        questionType: QuestionType,
        questionState: QuestionState
    ): Flow<BaseResult<List<ChatRoomVO>, String>> =
        repository.getRoomList(questionType, questionState)
}
package org.softwaremaestro.domain.chat

import kotlinx.coroutines.flow.Flow
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.domain.common.BaseResult

interface ChatRepository {
    suspend fun getRoomList(isTeacher: Boolean): Flow<BaseResult<ChatRoomListVO, String>>
}
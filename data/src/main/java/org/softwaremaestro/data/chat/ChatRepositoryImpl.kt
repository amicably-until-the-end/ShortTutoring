package org.softwaremaestro.data.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.chat.model.asDomain
import org.softwaremaestro.data.chat.remote.ChatApi
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val chatApi: ChatApi) :
    ChatRepository {


    override suspend fun getRoomList(
        questionType: QuestionType,
        questionState: QuestionState
    ): Flow<BaseResult<List<ChatRoomVO>, String>> {
        return flow {
            val result = chatApi.getRoomList(questionType, questionState)

            if (result.isSuccessful && result.body()?.success == true) {
                emit(BaseResult.Success(result.body()?.data?.map { it.asDomain() }!!))
            } else {
                emit(BaseResult.Error("Error"))
            }
        }
    }
}
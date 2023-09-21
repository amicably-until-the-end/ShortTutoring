package org.softwaremaestro.data.chat

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.chat.model.ChatRoomListDto
import org.softwaremaestro.data.chat.model.asDomain
import org.softwaremaestro.data.chat.remote.ChatApi
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.QuestionType
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val chatApi: ChatApi) :
    ChatRepository {


    override suspend fun getRoomList(): Flow<BaseResult<ChatRoomListVO, String>> {
        return flow {
            val result = chatApi.getRoomList()

            if (result.isSuccessful && result.body()?.success == true) {
                val data = result.body()?.data!!
                val chatRoomListVO = data.asDomain()
                emit(BaseResult.Success(chatRoomListVO))

            } else {
                emit(BaseResult.Error("Error"))
            }
        }
    }
}
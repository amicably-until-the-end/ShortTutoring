package org.softwaremaestro.data.chat

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.chat.database.ChatDatabase
import org.softwaremaestro.data.chat.model.ChatRoomListDto
import org.softwaremaestro.data.chat.model.asDomain as DTOToVO
import org.softwaremaestro.data.chat.entity.asDomain as EntityToVO
import org.softwaremaestro.data.chat.remote.ChatApi
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.RoomType
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi, private val chatDatabase: ChatDatabase
) :
    ChatRepository {


    private fun getRoomFromDB(isTeacher: Boolean): ChatRoomListVO {
        var proposedNormal =
            chatDatabase.chatRoomDao().getProposedNormalChatRoom().map { it.EntityToVO() }
        var proposedSelect =
            chatDatabase.chatRoomDao().getProposedSelectChatRoom().map { it.EntityToVO() }
        var reservedNormal =
            chatDatabase.chatRoomDao().getReservedNormalChatRoom().map { it.EntityToVO() }
        var reservedSelect =
            chatDatabase.chatRoomDao().getReservedSelectChatRoom().map { it.EntityToVO() }

        val groups: MutableList<ChatRoomVO> = mutableListOf()
        if (!isTeacher) {
            proposedNormal.groupBy { it.questionId }.forEach {
                val questionRoom = ChatRoomVO(
                    roomType = RoomType.QUESTION,
                    roomImage = "questionImage",
                    title = "questionTitle",
                    schoolLevel = "schoolLevel",
                    schoolSubject = "schoolSubject",
                    isSelect = false,
                    questionId = "questionId",
                    questionState = QuestionState.PROPOSED,
                    teachers = it.value,
                )
                groups.add(questionRoom)
            }
        }
        return ChatRoomListVO(
            if (isTeacher) proposedNormal else groups,
            proposedSelect,
            reservedNormal, reservedSelect,
        )
    }


    override suspend fun getRoomList(isTeacher: Boolean): Flow<BaseResult<ChatRoomListVO, String>> {
        return flow {
            emit(BaseResult.Success(getRoomFromDB(isTeacher)))
            /*
            val result = chatApi.getRoomList()


            if (result.isSuccessful && result.body()?.success == true) {
                val data = result.body()?.data!!
                val chatRoomListVO = data.DTOToVO()
                emit(BaseResult.Success(chatRoomListVO))

            } else {
                emit(BaseResult.Error("Error"))
            }

             */
        }
    }
}
package org.softwaremaestro.data.chat

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.chat.database.ChatDatabase
import org.softwaremaestro.data.chat.entity.MessageEntity
import org.softwaremaestro.data.chat.entity.asEntity
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


    private fun getRoomFromDB(isTeacher: Boolean): ChatRoomListVO? {
        try {
            var proposedNormal =
                chatDatabase.chatRoomDao().getProposedNormalChatRoom().map { it.EntityToVO() }
            var proposedSelect =
                chatDatabase.chatRoomDao().getProposedSelectChatRoom().map { it.EntityToVO() }
            var reservedNormal =
                chatDatabase.chatRoomDao().getReservedNormalChatRoom().map { it.EntityToVO() }
            var reservedSelect =
                chatDatabase.chatRoomDao().getReservedSelectChatRoom().map { it.EntityToVO() }

            Log.d("ChatRepositoryImpl chat", "일반 ${proposedNormal.toString()}")

            val groups: MutableList<ChatRoomVO> = mutableListOf()
            if (!isTeacher) {
                //학생이면 그룹화
                proposedNormal.groupBy { it.questionId }.forEach { group ->
                    val questionRoom = ChatRoomVO(
                        roomType = RoomType.QUESTION,
                        roomImage = "questionImage",
                        title = "questionTitle",
                        schoolLevel = "schoolLevel",
                        schoolSubject = "schoolSubject",
                        isSelect = false,
                        questionId = "questionId",
                        questionState = QuestionState.PROPOSED,
                        teachers = group.value.mapNotNull { if (it.opponentId != null) it else null }
                    )
                    groups.add(questionRoom)
                }
                Log.d("ChatRepositoryImpl group", groups.toString())
            }
            return ChatRoomListVO(
                if (isTeacher) proposedNormal else groups,
                proposedSelect,
                reservedNormal, reservedSelect,
            )
        } catch (e: Exception) {
            Log.d("ChatRepositoryImpl", e.toString())
            return null;
        }

    }

    private suspend fun updateRoomStatus() {
        val result = chatApi.getRoomList()

        Log.d("ChatRepositoryImpl  update", result.toString())
        if (result.isSuccessful && result.body()?.success == true) {
            val data = result.body()?.data!!
            data.map {
                chatDatabase.chatRoomDao().insert(it.asEntity())
            }
        }
    }


    override suspend fun getRoomList(isTeacher: Boolean): Flow<BaseResult<ChatRoomListVO, String>> {
        return flow {
            //updateRoomStatus()
            var result = getRoomFromDB(isTeacher)
            Log.d("ChatRepositoryImpl", result.toString())
            if (result == null) {
                emit(BaseResult.Error("error"))
            } else {
                emit(BaseResult.Success(result))
            }

        }
    }

    override suspend fun insertMessage(
        roomId: String,
        body: String,
        format: String,
        sendAt: String,
        isMyMsg: Boolean
    ) {
        try {
            chatDatabase.messageDao().insert(
                MessageEntity(
                    id = roomId + sendAt,
                    roomId = roomId,
                    body = body,
                    format = format,
                    isRead = isMyMsg,
                    sendAt = java.time.LocalDateTime.now(),
                    isMyMsg = isMyMsg
                )
            )

        } catch (e: Exception) {
            Log.d("ChatRepositoryImpl", e.toString())
        }
    }
}
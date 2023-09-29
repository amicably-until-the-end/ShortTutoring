package org.softwaremaestro.data.chat

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.softwaremaestro.data.chat.database.ChatDatabase
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.ChatRoomType
import org.softwaremaestro.data.chat.entity.MessageEntity
import org.softwaremaestro.data.chat.entity.asEntity
import org.softwaremaestro.data.chat.entity.asDomain as EntityToVO
import org.softwaremaestro.data.chat.remote.ChatApi
import org.softwaremaestro.data.question_get.remote.QuestionGetApi
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.RoomType
import org.softwaremaestro.domain.common.BaseResult
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val chatDatabase: ChatDatabase,
    private val questionApi: QuestionGetApi,
) :
    ChatRepository {


    private suspend fun getRoomFromDB(isTeacher: Boolean): ChatRoomListVO? {
        try {
            var proposedNormal =
                chatDatabase.chatRoomDao().getChatRoomByGroupType(ChatRoomType.PROPOSED_NORMAL.type)
                    .map { it.EntityToVO() }
            var proposedSelect =
                chatDatabase.chatRoomDao().getChatRoomByGroupType(ChatRoomType.PROPOSED_SELECT.type)
                    .map { it.EntityToVO() }
            var reservedNormal =
                chatDatabase.chatRoomDao().getChatRoomByGroupType(ChatRoomType.RESERVED_NORMAL.type)
                    .map { it.EntityToVO() }
            var reservedSelect =
                chatDatabase.chatRoomDao().getChatRoomByGroupType(ChatRoomType.RESERVED_SELECT.type)
                    .map { it.EntityToVO() }


            Log.d("ChatRepositoryImpl chat", "일반 ${proposedNormal} 예약 ${reservedNormal}")

            val groups: MutableList<ChatRoomVO> = mutableListOf()
            if (!isTeacher) {
                //학생이면 그룹화
                proposedNormal.groupBy { it.questionId }.forEach { group ->
                    val questionInfo = questionApi.getQuestionInfo(group.key!!).body()?.data
                    Log.d("ChatRepositoryImpl info", questionInfo.toString())
                    questionInfo?.let {
                        val questionRoom = ChatRoomVO(
                            roomType = RoomType.QUESTION,
                            roomImage = questionInfo.problemDto?.mainImage,
                            title = questionInfo.problemDto?.description,
                            schoolLevel = questionInfo.problemDto?.schoolLevel,
                            schoolSubject = questionInfo.problemDto?.schoolSubject,
                            isSelect = false,
                            questionId = group.key,
                            questionState = QuestionState.PROPOSED,
                            description = group.value[0].description,
                            teachers = group.value.filter { it.opponentId != null },
                        )
                        groups.add(questionRoom)
                    }
                }
                Log.d("ChatRepositoryImpl group", groups.toString())
            }
            return ChatRoomListVO(
                if (isTeacher) proposedNormal else groups,
                reservedNormal, proposedSelect, reservedSelect
            )
        } catch (e: Exception) {
            Log.d("ChatRepositoryImpl", e.toString())
            return null;
        }

    }

    private suspend fun updateRoomStatus() {
        val result = chatApi.getRoomList()

        Log.d("ChatRepositoryImpl  update", result.body().toString())
        if (result.isSuccessful && result.body()?.success == true) {
            val data = result.body()?.data!!
            data.map {
                insertOrUpdateRoom(it.asEntity())
            }
        }
    }

    private fun insertOrUpdateRoom(room: ChatRoomEntity) {
        if (chatDatabase.chatRoomDao().isIdExist(room.id)) {
            chatDatabase.chatRoomDao().update(room)
        } else {
            chatDatabase.chatRoomDao().insert(room)
        }

    }


    override suspend fun getRoomList(isTeacher: Boolean): Flow<BaseResult<ChatRoomListVO, String>> {
        return flow {
            updateRoomStatus()
            var result = getRoomFromDB(isTeacher)
            Log.d("ChatRepositoryImpl", result.toString())
            if (result == null) {
                emit(BaseResult.Error("error"))
            } else {
                emit(BaseResult.Success(result))
            }
        }
    }

    override suspend fun getMessages(chatRoomId: String): Flow<BaseResult<List<MessageVO>, String>> {
        return flow {
            var result = chatDatabase.chatRoomDao().getChatRoomWithMessages(chatRoomId)
            Log.d("ChatRepositoryImpl", "chatroomId $chatRoomId $result")
            if (result == null) {
                emit(BaseResult.Error("error"))
            } else {
                emit(BaseResult.Success(result.messages.map { it.EntityToVO() }))
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
            Log.d("ChatRepositoryImpl", "insert ${roomId} $body")
            if (!chatDatabase.chatRoomDao().isIdExist(roomId)) {
                var result = chatApi.getRoom(roomId)
                Log.d(
                    "ChatRepositoryImpl",
                    "insert data entity ${result.body()}"
                )
                result.body()?.data?.let { chatDatabase.chatRoomDao().insert(it.asEntity()) }
            }
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
            Log.d("ChatRepositoryImpl insertMessage", e.toString())
        }
    }
}
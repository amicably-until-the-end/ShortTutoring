package org.softwaremaestro.data.chat.entity

import com.google.gson.Gson
import org.softwaremaestro.data.chat.model.ChatRoomDto
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageBodyVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.RoomType
import java.time.LocalDateTime
import java.time.ZoneId

class Mapper {

    fun asDomain(chatRoomEntity: ChatRoomEntity): ChatRoomVO {
        chatRoomEntity.apply {
            return ChatRoomVO(
                id = id,
                title = title,
                schoolLevel = "fff",
                schoolSubject = "fff",
                roomType = RoomType.TEACHER,
                roomImage = image,
                questionId = "fff",
                isSelect = true,
                questionState =
                if (status == 1 || status == 2)
                    QuestionState.PROPOSED else QuestionState.RESERVED,
            )
        }
    }

    fun asDomain(messageEntity: MessageEntity): MessageVO {
        messageEntity.apply {
            var bodyVO: MessageBodyVO;
            val gson = Gson()
            try {
                when (format) {
                    "text" -> bodyVO = gson.fromJson(body, MessageBodyVO.Text::class.java)
                    "problem-image" -> bodyVO =
                        gson.fromJson(body, MessageBodyVO.ProblemImage::class.java)

                    "appoint-request" -> bodyVO =
                        gson.fromJson(body, MessageBodyVO.AppointRequest::class.java)

                    else -> bodyVO = MessageBodyVO.Text(body)
                }
            } catch (e: Exception) {
                bodyVO = MessageBodyVO.Text(body)
            }
            return MessageVO(
                time = sendAt,
                bodyVO = bodyVO,
                isMyMsg = isMyMsg,
            )
        }
    }

    fun asDomain(chatRoomWithMessages: ChatRoomWithMessages): ChatRoomVO {
        chatRoomWithMessages.apply {
            return ChatRoomVO(
                id = chatRoomEntity.id,
                title = chatRoomEntity.title,
                schoolLevel = "fff",
                schoolSubject = "fff",
                roomType = if (chatRoomEntity.opponentId != null) RoomType.TEACHER else RoomType.QUESTION,
                roomImage = chatRoomEntity.image,
                questionId = "fff",
                isSelect = true,
                questionState =
                if (chatRoomEntity.status == 1 || chatRoomEntity.status == 2)
                    QuestionState.PROPOSED else QuestionState.RESERVED,
                messages = messages.map { it.asDomain() },
                opponentId = chatRoomEntity.opponentId,
            )
        }
    }

    fun asEntity(dto: ChatRoomDto): ChatRoomEntity {
        return ChatRoomEntity(
            id = dto.id!!,
            title = dto.title,
            image = dto.roomImage,
            status = 1,
            startDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
            opponentId = dto.opponentId,
        )
    }
}

fun ChatRoomEntity.asDomain(): ChatRoomVO {
    return Mapper().asDomain(this)
}

fun MessageEntity.asDomain(): MessageVO {
    return Mapper().asDomain(this)
}

fun ChatRoomWithMessages.asDomain(): ChatRoomVO {
    return Mapper().asDomain(this)
}

fun ChatRoomDto.asEntity(): ChatRoomEntity {
    return Mapper().asEntity(this)
}
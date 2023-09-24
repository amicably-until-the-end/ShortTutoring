package org.softwaremaestro.data.chat.entity

import org.softwaremaestro.data.chat.model.ChatRoomDto
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
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
            return MessageVO(
                time = createdAt,
                bodyVO = null,
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
                roomType = RoomType.TEACHER,
                roomImage = chatRoomEntity.image,
                questionId = "fff",
                isSelect = true,
                questionState =
                if (chatRoomEntity.status == 1 || chatRoomEntity.status == 2)
                    QuestionState.PROPOSED else QuestionState.RESERVED,
                messages = messages.map { it.asDomain() }
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
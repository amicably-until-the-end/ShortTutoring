package org.softwaremaestro.data.chat.model

import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.RoomType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Mapper {

    fun asDomain(chatRoomListDto: ChatRoomListDto): ChatRoomListVO {
        chatRoomListDto.apply {
            return ChatRoomListVO(
                normalProposed = normalProposed.map { it.asDomain() },
                normalReserved = normalReserved.map { it.asDomain() },
                selectedProposed = selectedProposed.map { it.asDomain() },
                selectedReserved = selectedReserved.map { it.asDomain() },
            )
        }
    }

    fun asDomain(chatRoomDto: ChatRoomDto): ChatRoomVO {
        chatRoomDto.apply {
            return ChatRoomVO(
                id = id,
                questionState = if (questionState == "pending") QuestionState.PROPOSED else QuestionState.RESERVED,
                opponentId = opponentId,
                title = title,
                messages = listOf(),
                roomImage = roomImage,
                roomType = if (opponentId != null) RoomType.TEACHER else RoomType.QUESTION,
                isSelect = isSelect ?: false,
                questionId = questionId,
                description = questionInfo?.problem?.description ?: "",
            )
        }
    }

    /*
    fun asDomain(messageDto: MessageDto): MessageVO {
        Log.d("chat", messageDto.toString())

        var bodyVO = when (messageDto.format) {
            "text" -> MessageBodyVO.Text(messageDto.body.text)
            "problem-image" -> MessageBodyVO.ProblemImage(
                messageDto.body.imageUrl,
                messageDto.body.description
            )

            "appoint-request" -> {
                MessageBodyVO.AppointRequest(messageDto.body.startDateTime)
            }

            else -> MessageBodyVO.Text(messageDto.body.text)
        }
        var time = ISOParser(messageDto.time)
        return MessageVO(
            time = time,
            bodyVO = bodyVO,
            sender = messageDto.sender,
            isMyMsg = messageDto.isMyMsg,
        )
    }*/

    fun asDomain(chatRoomEntity: ChatRoomEntity): ChatRoomVO {
        chatRoomEntity.apply {
            return ChatRoomVO(
                id = id,
                title = title,
                roomType = RoomType.TEACHER,
                roomImage = image,
                questionId = questionId,
                isSelect = true,
                description = description ?: "undefined",
                startDateTime = startDateTime,
                questionState =
                if (status == 1 || status == 2)
                    QuestionState.PROPOSED else QuestionState.RESERVED,
            )
        }
    }

    companion object {
        fun ISOParser(text: String): LocalDateTime {
            val format: DateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            var dateTime = LocalDateTime.parse(
                text,
                format
            )
            dateTime = dateTime.plusHours(9)
            return dateTime
        }
    }
}

fun ChatRoomDto.asDomain(): ChatRoomVO {
    return Mapper().asDomain(this)
}

fun ChatRoomListDto.asDomain(): ChatRoomListVO {
    return Mapper().asDomain(this)
}

/*
fun MessageDto.asDomain(): MessageVO {
    return Mapper().asDomain(this)
}*/

fun ChatRoomEntity.asDomain(): ChatRoomVO {
    return Mapper().asDomain(this)
}
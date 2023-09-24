package org.softwaremaestro.data.chat.model

import android.util.Log
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageBodyVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.RoomType
import org.softwaremaestro.domain.chat.entity.StudentInfoVO
import org.softwaremaestro.domain.chat.entity.TeacherInfoVO
import java.time.Instant
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
                id = tutoringId,
                questionState = if (questionState == "pending") QuestionState.PROPOSED else QuestionState.RESERVED,
                opponentId = opponentId,
                title = title,
                schoolSubject = schoolSubject,
                schoolLevel = schoolLevel,
                messages = messages?.map { it.asDomain() },
                roomImage = roomImage,
                roomType = if (isTeacherRoom == true) RoomType.TEACHER else RoomType.QUESTION,
                teachers = teachers?.map { it.asDomain() },
                isSelect = isSelect ?: false,
                questionId = questionId,
            )
        }
    }

    fun asDomain(messageDto: MessageDto): MessageVO {
        Log.d("chat", messageDto.toString())

        var bodyVO = when (messageDto.format) {
            "text" -> MessageBodyVO.Text(messageDto.body.text)
            "problem-image" -> MessageBodyVO.ProblemImage(
                messageDto.body.imageUrl,
                messageDto.body.description
            )

            "appoint-request" -> {
                var dateTime = ISOParser(messageDto.body.startDateTime!!)
                MessageBodyVO.AppointRequest(dateTime)
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
    }

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

fun MessageDto.asDomain(): MessageVO {
    return Mapper().asDomain(this)
}

fun ChatRoomEntity.asDomain(): ChatRoomVO {
    return Mapper().asDomain(this)
}
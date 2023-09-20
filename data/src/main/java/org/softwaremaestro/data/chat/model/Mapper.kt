package org.softwaremaestro.data.chat.model

import android.util.Log
import org.softwaremaestro.domain.chat.entity.ChatRoomListVO
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageBodyVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.RoomType
import org.softwaremaestro.domain.chat.entity.StudentInfoVO
import org.softwaremaestro.domain.chat.entity.TeacherInfoVO
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
                questionState = if (questionState == "PROPOSED") QuestionState.PROPOSED else QuestionState.RESERVED,
                opponentId = opponentId,
                title = title,
                schoolSubject = schoolSubject,
                schoolLevel = schoolLevel,
                messages = messages?.map { it.asDomain() },
                roomImage = roomImage,
                roomType = if (isSelect == true) RoomType.TEACHER else RoomType.QUESTION,
                teachers = null,
            )
        }
    }

    fun asDomain(messageDto: MessageDto): MessageVO {
        Log.d("chat", messageDto.toString())

        var bodyVO = when (messageDto.format) {
            "problem-image" -> MessageBodyVO.Text(messageDto.body.text)
            "text" -> MessageBodyVO.ProblemImage(
                messageDto.body.imageUrl,
                messageDto.body.description
            )

            "appoint-request" -> {
                var dateTime = LocalDateTime.parse(
                    messageDto.body.startDateTime,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                )
                MessageBodyVO.AppointRequest(dateTime)
            }

            else -> MessageBodyVO.Text(messageDto.body.text)
        }
        return MessageVO(
            time = messageDto.time,
            bodyVO = bodyVO,
            sender = messageDto.sender,
        )
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

package org.softwaremaestro.data.chat.model

import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionInfoVO
import org.softwaremaestro.domain.chat.entity.StudentInfoVO
import org.softwaremaestro.domain.chat.entity.TeacherInfoVO

class Mapper {
    fun asDomain(chatRoomDto: ChatRoomDto): ChatRoomVO {
        return ChatRoomVO(
            id = chatRoomDto.tutoringId,
            questionInfo = chatRoomDto.questionInfo.asDomain(),
            studentInfo = chatRoomDto.studentInfo.asDomain(),
            teacherInfo = chatRoomDto.teacherInfo.asDomain(),
            roomType = chatRoomDto.roomType,
        )
    }

    fun asDomain(teacherInfoDto: TeacherInfoDto): TeacherInfoVO {
        return TeacherInfoVO(
            id = teacherInfoDto.id,
            name = teacherInfoDto.name,
            profileImageUrl = teacherInfoDto.profileImageUrl
        )
    }

    fun asDomain(studentInfoDto: StudentInfoDto): StudentInfoVO {
        return StudentInfoVO(
            id = studentInfoDto.id,
            name = studentInfoDto.name,
            profileImageUrl = studentInfoDto.profileImageUrl
        )
    }

    fun asDomain(questionInfoDto: QuestionInfoDto): QuestionInfoVO {
        return QuestionInfoVO(
            id = questionInfoDto.id,
            title = questionInfoDto.title,
            content = questionInfoDto.content,
            imageUrl = questionInfoDto.imageUrl,
            category = questionInfoDto.category,
            createdAt = questionInfoDto.createdAt,
            updatedAt = questionInfoDto.updatedAt,
            isAnswered = questionInfoDto.isAnswered,
            isDeleted = questionInfoDto.isDeleted
        )
    }
}

fun ChatRoomDto.asDomain(): ChatRoomVO {
    return Mapper().asDomain(this)
}

fun TeacherInfoDto.asDomain(): TeacherInfoVO {
    return Mapper().asDomain(this)
}

fun StudentInfoDto.asDomain(): StudentInfoVO {
    return Mapper().asDomain(this)
}

fun QuestionInfoDto.asDomain(): QuestionInfoVO {
    return Mapper().asDomain(this)
}


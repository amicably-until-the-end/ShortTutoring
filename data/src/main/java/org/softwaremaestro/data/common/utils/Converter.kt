package org.softwaremaestro.data.common.utils

import androidx.room.TypeConverter
import org.softwaremaestro.data.chat.entity.ChatRoomType
import java.time.LocalDateTime

class Converter {
    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromInt(value: Int?): ChatRoomType? {
        return when (value) {
            0 -> ChatRoomType.PROPOSED_NORMAL
            1 -> ChatRoomType.PROPOSED_SELECT
            2 -> ChatRoomType.RESERVED_NORMAL
            3 -> ChatRoomType.RESERVED_SELECT
            else -> null
        }
    }

    @TypeConverter
    fun fromChatRoomType(type: ChatRoomType?): Int? {
        return type?.type
    }
}
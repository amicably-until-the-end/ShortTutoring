package org.softwaremaestro.data.chat.entity

import androidx.room.TypeConverter
import org.softwaremaestro.domain.chat.entity.ChatRoomState
import java.time.LocalDateTime

class Converter {
    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun fromChatRoomState(state: ChatRoomState): String {
        return state.value
    }

    @TypeConverter
    fun toChatRoomState(value: String): ChatRoomState {
        return ChatRoomState.values().find { it.value == value } ?: ChatRoomState.PROPOSED
    }
}
package org.softwaremaestro.data.chat.entity

import android.system.StructMsghdr
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime
import java.time.ZoneId

@Entity
data class ChatRoomEntity(
    @PrimaryKey var id: String,
    var title: String,
    var startDateTime: LocalDateTime,
    var type: ChatRoomType,
)

@Entity
data class ChatRoomWithMessages(
    @Embedded val chatRoomEntity: ChatRoomEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "roomId"
    )
    val messages: List<MessageEntity>
)

enum class ChatRoomType(val type: Int) {
    PROPOSED_NORMAL(type = 0),
    PROPOSED_SELECT(type = 1),
    RESERVED_NORMAL(type = 2),
    RESERVED_SELECT(type = 3),
}
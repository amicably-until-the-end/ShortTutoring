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
    var status: Int,
    var image: String,
) {
    companion object {
        const val PROPOSED_NORMAL = 0
        const val PROPOSED_SELECT = 1
        const val RESERVED_NORMAL = 2
        const val RESERVED_SELECT = 3
    }
}

@Entity
data class ChatRoomWithMessages(
    @Embedded val chatRoomEntity: ChatRoomEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "roomId"
    )
    val messages: List<MessageEntity>
)
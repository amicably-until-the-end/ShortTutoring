package org.softwaremaestro.data.chat.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChatRoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MessageEntity(
    @PrimaryKey var id: String,
    var roomId: String,
    var format: String,
    var image: String,
    var description: String,
    var startDateTime: LocalDateTime,
    var isRead: Boolean = false,
    var isMyMsg: Boolean,
    var createdAt: LocalDateTime,
    var questionId: String,
)
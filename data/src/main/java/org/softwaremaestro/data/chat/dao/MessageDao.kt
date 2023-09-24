package org.softwaremaestro.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.MessageEntity

@Dao
interface MessageDao {

    @Insert
    fun insert(messageEntity: MessageEntity)
}
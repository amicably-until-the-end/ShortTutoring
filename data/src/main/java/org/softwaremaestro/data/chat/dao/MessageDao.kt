package org.softwaremaestro.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(messageEntity: MessageEntity)

    @Query("SELECT * FROM MessageEntity")
    fun getMessages(): List<MessageEntity>


}
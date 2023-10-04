package org.softwaremaestro.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.softwaremaestro.data.chat.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(messageEntity: MessageEntity)

    @Query("SELECT * FROM MessageEntity")
    fun getMessages(): List<MessageEntity>

    @Query("UPDATE MessageEntity SET isRead = 1 WHERE roomId = :chatRoomId")
    fun markAsRead(chatRoomId: String): Int

}
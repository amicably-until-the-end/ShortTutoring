package org.softwaremaestro.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.ChatRoomType
import org.softwaremaestro.data.chat.entity.ChatRoomWithMessages
import org.softwaremaestro.domain.chat.entity.ChatRoomVO

@Dao
interface ChatRoomDao {

    @Query("SELECT * FROM ChatRoomEntity")
    fun getAll(): List<ChatRoomEntity>

    @Query("SELECT * FROM ChatRoomEntity WHERE id = :roomId")
    fun getChatRoom(roomId: String): ChatRoomEntity

    @Query("DELETE FROM ChatRoomEntity")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM ChatRoomEntity WHERE id = :roomId)")
    fun isIdExist(roomId: String): Boolean

    @Insert()
    fun insert(chatRoomEntity: ChatRoomEntity)

    @Update
    fun update(chatRoomEntity: ChatRoomEntity)

    @Query("SELECT * FROM ChatRoomEntity")
    fun getChatRoomWithMessages(): List<ChatRoomWithMessages>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = :type")
    fun getChatRoomByGroupType(type: Int): List<ChatRoomWithMessages>


}
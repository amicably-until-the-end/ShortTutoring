package org.softwaremaestro.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.ChatRoomWithMessages

@Dao
interface ChatRoomDao {

    @Query("SELECT * FROM ChatRoomEntity")
    fun getAll(): List<ChatRoomEntity>

    @Query("SELECT * FROM ChatRoomEntity WHERE id = :roomId")
    fun getChatRoom(roomId: String): ChatRoomEntity

    @Query("DELETE FROM ChatRoomEntity")
    fun deleteAll()

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    fun insert(chatRoomEntity: ChatRoomEntity)


    @Query("SELECT * FROM ChatRoomEntity")
    fun getChatRoomWithMessages(): List<ChatRoomEntity>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = 0")
    fun getProposedSelectChatRoom(): List<ChatRoomWithMessages>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = 1")
    fun getProposedNormalChatRoom(): List<ChatRoomWithMessages>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = 2")
    fun getReservedSelectChatRoom(): List<ChatRoomWithMessages>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = 3")
    fun getReservedNormalChatRoom(): List<ChatRoomWithMessages>


}
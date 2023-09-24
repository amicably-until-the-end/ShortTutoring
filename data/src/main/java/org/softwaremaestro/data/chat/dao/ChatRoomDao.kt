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

    @Insert
    fun insert(chatRoomEntity: ChatRoomEntity)

    @Query("SELECT * FROM ChatRoomEntity")
    fun getChatRoomWithMessages(): List<ChatRoomEntity>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = ${ChatRoomEntity.PROPOSED_SELECT}")
    fun getProposedSelectChatRoom(): List<ChatRoomWithMessages>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = ${ChatRoomEntity.PROPOSED_NORMAL}")
    fun getProposedNormalChatRoom(): List<ChatRoomWithMessages>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = ${ChatRoomEntity.RESERVED_SELECT}")
    fun getReservedSelectChatRoom(): List<ChatRoomWithMessages>

    @Query("SELECT * FROM ChatRoomEntity WHERE status = ${ChatRoomEntity.RESERVED_NORMAL}")
    fun getReservedNormalChatRoom(): List<ChatRoomWithMessages>


}
package org.softwaremaestro.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.ChatRoomWithMessages
import org.softwaremaestro.data.chat.entity.ChatRoomWithUnReadMessageCnt
import java.time.LocalDateTime

@Dao
interface ChatRoomDao {

    @Query("SELECT * FROM ChatRoomEntity")
    fun getAll(): List<ChatRoomEntity>

    @Query("SELECT * FROM ChatRoomEntity WHERE id = :roomId")
    fun getChatRoom(roomId: String): ChatRoomEntity

    @Query("DELETE FROM ChatRoomEntity")
    fun deleteAll()

    @Query("DELETE FROM ChatRoomEntity WHERE id = :roomId")
    fun delete(roomId: String)

    @Query("SELECT EXISTS(SELECT * FROM ChatRoomEntity WHERE id = :roomId)")
    fun isIdExist(roomId: String): Boolean

    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    fun insert(chatRoomEntity: ChatRoomEntity)

    @Update
    fun update(chatRoomEntity: ChatRoomEntity)

    @Query("SELECT * FROM ChatRoomEntity")
    fun getChatRoomsWithMessages(): List<ChatRoomWithMessages>

    @Query("SELECT * FROM ChatRoomEntity WHERE id = :chattingId")
    fun getChatRoomWithMessages(chattingId: String): ChatRoomWithMessages

    @Query("SELECT * FROM ChatRoomEntity WHERE status = :type")
    fun getChatRoomByGroupType(type: Int): List<ChatRoomWithMessages>

    @Query("SELECT ChatRoomEntity.*, COUNT(MessageEntity.id) AS unReadCnt FROM ChatRoomEntity LEFT JOIN MessageEntity ON MessageEntity.roomId = ChatRoomEntity.id AND MessageEntity.isRead = 0 WHERE ChatRoomEntity.status = :type GROUP BY ChatRoomEntity.id ORDER BY lastMessageTime DESC")
    fun getChatRoomListWithUnReadCnt(type: Int): List<ChatRoomWithUnReadMessageCnt>

    @Query("UPDATE ChatRoomEntity SET lastMessageTime = :time WHERE id = :id")
    fun updateLastMessageTime(id: String, time: LocalDateTime)
}
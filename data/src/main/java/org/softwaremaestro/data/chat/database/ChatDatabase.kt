package org.softwaremaestro.data.chat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.softwaremaestro.data.chat.dao.ChatRoomDao
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.Converter
import org.softwaremaestro.data.chat.entity.MessageEntity


@Database(
    entities = [ChatRoomEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatRoomDao(): ChatRoomDao

    companion object {
        private var instance: ChatDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ChatDatabase? {
            if (instance == null) {
                synchronized(ChatDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatDatabase::class.java, "chat.db"
                    ).build()
                }
            }
            return instance
        }
    }
}
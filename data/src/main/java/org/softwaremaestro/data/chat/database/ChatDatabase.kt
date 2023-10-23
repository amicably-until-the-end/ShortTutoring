package org.softwaremaestro.data.chat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.softwaremaestro.data.chat.dao.ChatRoomDao
import org.softwaremaestro.data.chat.dao.MessageDao
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.Converter
import org.softwaremaestro.data.chat.entity.MessageEntity


@Database(
    entities = [ChatRoomEntity::class, MessageEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatRoomDao(): ChatRoomDao
    abstract fun messageDao(): MessageDao


    companion object {
        private var instance: ChatDatabase? = null

        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ChatRoomEntity ADD COLUMN lastMessageTime TEXT DEFAULT '2021-09-02T14:56:20.669'")
            }
        }

        private val migration_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ChatRoomEntity ADD COLUMN questionImage TEXT DEFAULT ''")
            }
        }

        @Synchronized
        fun getInstance(context: Context): ChatDatabase? {
            if (instance == null) {
                synchronized(ChatDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatDatabase::class.java, "chat.db"
                    ).addMigrations(migration_1_2).addMigrations(migration_2_3)
                        .build()
                }
            }
            return instance
        }
    }

}
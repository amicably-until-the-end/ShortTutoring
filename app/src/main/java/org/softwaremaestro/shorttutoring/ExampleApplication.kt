package org.softwaremaestro.shorttutoring

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.softwaremaestro.data.chat.database.ChatDatabase
import org.softwaremaestro.data.chat.entity.ChatRoomEntity
import org.softwaremaestro.data.chat.entity.ChatRoomType
import java.time.LocalDate
import java.time.LocalDateTime

@HiltAndroidApp
class ExampleApplication : Application() {

    private lateinit var db: ChatDatabase
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        clearDataBase()
    }


    private fun clearDataBase() {
        db = ChatDatabase.getInstance(this)!!
        CoroutineScope(Dispatchers.IO).launch {
            db.chatRoomDao().deleteAll()
        }
    }

    private fun initDataBase() {

        db = ChatDatabase.getInstance(this)!!
        CoroutineScope(Dispatchers.IO).launch {
            db.chatRoomDao().insert(
                ChatRoomEntity(
                    "2",
                    "가짜 채팅방",
                    LocalDateTime.now(),
                    ChatRoomType.PROPOSED_NORMAL
                )
            )
            var a = db.chatRoomDao().getAll()
            Log.d("rooom", a.toString())
        }
    }

}
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
import java.time.LocalDate
import java.time.LocalDateTime
import io.socket.client.IO
import io.socket.client.Socket
import org.softwaremaestro.data.chat.entity.MessageEntity
import org.softwaremaestro.domain.socket.SocketManager
import javax.inject.Inject

@HiltAndroidApp
class ExampleApplication : Application() {

    private lateinit var db: ChatDatabase
    private lateinit var socket: Socket

    @Inject
    lateinit var socketManager: SocketManager
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        initDataBase()
        initSocket()
    }


    private fun clearDataBase() {
        db = ChatDatabase.getInstance(this)!!
        CoroutineScope(Dispatchers.IO).launch {
            db.chatRoomDao().deleteAll()
        }
    }

    private fun initDataBase() {

        db = ChatDatabase.getInstance(this)!!

    }

    private fun initSocket() {
        socketManager.init()
    }


}
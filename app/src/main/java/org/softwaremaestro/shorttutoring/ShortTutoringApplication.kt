package org.softwaremaestro.shorttutoring

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.softwaremaestro.data.chat.database.ChatDatabase
import org.softwaremaestro.domain.socket.SocketManager
import javax.inject.Inject

@HiltAndroidApp
class ShortTutoringApplication : Application(), LifecycleEventObserver {

    private lateinit var db: ChatDatabase

    @Inject
    lateinit var socketManager: SocketManager

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        initDataBase()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
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

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                // 앱이 포그라운드로 들어올 때
                socketManager.init()
                isForeground = true
            }

            Lifecycle.Event.ON_STOP -> {
                // 앱이 백그라운드로 들어올 때
                isForeground = false
                socketManager.close()
            }

            else -> {}
        }
    }

    companion object {
        var isForeground = false
    }


}
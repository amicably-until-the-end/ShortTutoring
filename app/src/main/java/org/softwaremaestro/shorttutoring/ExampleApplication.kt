package org.softwaremaestro.shorttutoring

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.softwaremaestro.data.chat.database.ChatDatabase

@HiltAndroidApp
class ExampleApplication : Application() {

    private lateinit var db: ChatDatabase

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        initDataBase()
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


}
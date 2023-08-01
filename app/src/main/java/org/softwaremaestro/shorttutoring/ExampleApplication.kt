package org.softwaremaestro.shorttutoring

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "bbe58d8c5e26f4610575a18ef449c989")
    }

}
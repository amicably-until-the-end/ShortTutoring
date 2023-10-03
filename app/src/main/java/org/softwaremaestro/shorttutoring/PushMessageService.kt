package org.softwaremaestro.shorttutoring

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.login.usecase.LoginUseCase
import javax.inject.Inject

@AndroidEntryPoint
class PushMessageService :
    FirebaseMessagingService() {

    @Inject
    lateinit var loginUseCase: LoginUseCase


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("fcm", "onNewToken in PushMessageService")
        loginUseCase.saveFCMToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(
            "fcm",
            "onMessageReceived ${remoteMessage.notification?.body} ${ExampleApplication.isForeground}"
        )
        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )
        var builder: NotificationCompat.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
            builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(applicationContext)
        }
        val title = remoteMessage.data["title"] ?: "undefined"
        val body = remoteMessage.data["body"] ?: "undefined"
        builder.setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.btn_radio)
        val notification: Notification = builder.build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, notification)
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
    }
}
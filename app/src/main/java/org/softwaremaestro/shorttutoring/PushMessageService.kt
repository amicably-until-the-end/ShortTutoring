package org.softwaremaestro.shorttutoring

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.login.usecase.LoginUseCase
import org.softwaremaestro.domain.socket.SocketManager
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class PushMessageService :
    FirebaseMessagingService() {

    @Inject
    lateinit var loginUseCase: LoginUseCase

    @Inject
    lateinit var socketManager: SocketManager

    @Inject
    lateinit var chatRepository: ChatRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )
        if (ShortTutoringApplication.isForeground) {
            return
        }
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
        if (true) {
            with(remoteMessage.data) {
                CoroutineScope(Dispatchers.IO).launch {
                    chatRepository.insertMessage(
                        "40146be0-6886-4eb6-8014-5684eba1b173",
                        get("body") ?: "테스트 insert",
                        get("format") ?: "text",
                        get("createdAt") ?: LocalDateTime.now().toString(),
                        get("sender") == SocketManager.userId,
                    )
                }
            }
        }

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

    enum class PayloadType(val value: String) {
        CHAT_MESSAGE("chat-message"),
        ALERT("alert"),
    }
}
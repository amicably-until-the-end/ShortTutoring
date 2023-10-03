package org.softwaremaestro.shorttutoring

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.util.Log
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
        if (ShortTutoringApplication.isForeground) {
            //앱이 포그라운드 상태일 때는 그냥 무시한다.
            return
        }
        when (remoteMessage.data["type"]) {
            PayloadType.CHAT_MESSAGE.value -> {
                insertChatMessage(remoteMessage.data)
                sendChatMessageNotification()
            }

            PayloadType.ALERT.value -> {
                //TODO : 알림 메시지
            }
        }
    }

    private fun sendChatMessageNotification() {
        sendNotification(null, "새로운 메시지가 도착했습니다.")
    }

    private fun insertChatMessage(data: Map<String, String>) {
        try {
            with(data) {
                CoroutineScope(Dispatchers.IO).launch {
                    chatRepository.insertMessage(
                        get("chattingId") ?: "",
                        get("body") ?: "",
                        get("format") ?: "text",
                        get("createdAt") ?: LocalDateTime.now().toString(),
                        get("sender") == SocketManager.userId,
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("PushMessageService", e.toString())
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
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
            return
        }
        notificationManager.notify(1, notification)
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
    }

    enum class PayloadType(val value: String) {
        CHAT_MESSAGE("chatting"),
        ALERT("announce"),
    }
}
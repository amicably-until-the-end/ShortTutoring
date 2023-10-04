package org.softwaremaestro.shorttutoring

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
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
import org.softwaremaestro.presenter.login.SplashActivity
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

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    private fun createChannel() {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("PushMessageService", "${remoteMessage.data}")
        if (ShortTutoringApplication.isForeground) {
            //앱이 포그라운드 상태일 때는 그냥 무시한다.
            return
        }
        when (remoteMessage.data["type"]) {
            PayloadType.CHAT_MESSAGE.value -> {
                insertChatMessage(remoteMessage.data)
                sendChatMessageNotification(remoteMessage.data)
            }

            PayloadType.ALERT.value -> {
                //TODO : 알림 메시지
            }
        }
    }

    private fun sendChatMessageNotification(data: Map<String, String>) {
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)

        val intent = Intent(applicationContext, SplashActivity::class.java)
        intent.putExtra(
            SplashActivity.APP_LINK_ARGS_CHAT_ID,
            "40146be0-6886-4eb6-8014-5684eba1b173"
        )

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            SplashActivity.CHAT_INTENT_FLAG,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        with(data) {
            builder.setContentTitle(get("title"))
                .setContentText(get("body"))
                .setSmallIcon(R.drawable.btn_radio)
                .setContentIntent(pendingIntent)
        }
        sendNotification(builder.build())
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

    private fun sendNotification(notification: Notification) {
        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )

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
        private const val CHANNEL_ID = "shot_tutoring"
        private const val CHANNEL_NAME = "숏과외"
    }

    enum class PayloadType(val value: String) {
        CHAT_MESSAGE("message"),
        ALERT("announce"),
    }
}
package org.softwaremaestro.domain.socket

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.socket.vo.MessageFormat
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers.IO as DispatchersIO


class SocketManager @Inject constructor(
    private val repository: ChatRepository,
    private val userRepository: LoginRepository,
) {


    fun init() {
        if (mSocket != null) return // 이미 초기화 되어있으면 return
        println("socket  init")

        CoroutineScope(DispatchersIO).launch {
            try {
                userRepository.getUserInfo().collect {
                    userId = when (it) {
                        is BaseResult.Success -> {
                            it.data.id
                        }

                        else -> null
                    }
                }

                var jwt = userRepository.getToken()
                if (jwt.isNullOrEmpty()) return@launch
                var header =
                    mapOf("Authorization" to listOf("Bearer ${userRepository.getToken()}"))
                println("socket header: $header")
                var options = IO.Options().apply {
                    extraHeaders = header
                    transports = arrayOf(WebSocket.NAME)
                }
                mSocket = IO.socket(uri, options)
                onMessageReceive()
                mSocket?.connect() ?: println("socket connect fail")
            } catch (e: Exception) {
                println("socket ${e.message}")
            }
        }

    }

    fun addHomeListener(listener: () -> Unit) {
        homeMessageAppendListener = listener
    }

    fun close() {
        mSocket?.disconnect()
        mSocket?.close()
        mSocket = null
    }

    fun getSocket(): Socket {
        return mSocket!!
    }

    fun addOnMessageReceiveListener(listener: (String) -> Unit) {
        messageAppendListener = listener
    }

    fun setChatRoomId(chattingId: String?) {
        currentChatRoom = chattingId
    }


    private fun onMessageReceive() {
        mSocket?.on("message") { args ->
            CoroutineScope(DispatchersIO).launch {
                try {
                    println("socket message: ${args[0]}")

                    val message = Json.decodeFromString<MessageFormat>(args[0].toString())

                    repository.insertMessage(
                        message.chattingId,
                        message.message.body,
                        message.message.format,
                        message.message.createdAt,
                        message.message.sender == userId,
                        (message.message.sender == userId) || (currentChatRoom == message.chattingId)
                    ).collect {
                        messageAppendListener?.let { it(message.chattingId) }
                        homeMessageAppendListener?.let { it() }
                    }
                } catch (e: Exception) {
                    println("socket message error: ${e.message}")
                }
            }
        }
        mSocket?.on("connect") {
            println("socket connect")
        }
        mSocket?.on("disconnect") {
            println("socket disconnect ${it}")
        }
    }

    companion object {
        private const val uri = "https://api.short-tutoring.com/"

        //private const val uri = "http://10.0.2.2:3000/"

        var mSocket: Socket? = null
        var userId: String? = null
        private var messageAppendListener: ((String) -> Unit)? = null
        private var homeMessageAppendListener: (() -> Unit)? = null
        var currentChatRoom: String? = null
    }
}
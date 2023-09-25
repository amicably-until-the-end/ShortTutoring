package org.softwaremaestro.domain.socket

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO as DispatchersIO
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.softwaremaestro.domain.chat.ChatRepository
import org.softwaremaestro.domain.login.LoginRepository
import org.softwaremaestro.domain.socket.vo.MessageFormat
import javax.inject.Inject


class SocketManager @Inject constructor(
    private val repository: ChatRepository,
    private val userRepository: LoginRepository,
) {


    fun init() {
        println("socket init")

        CoroutineScope(DispatchersIO).launch {
            try {
                var header =
                    mapOf("Authorization" to listOf("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZW5kb3IiOiJrYWthbyIsImF1dGhJZCI6bnVsbCwidXNlcklkIjoic3ktc3R1ZGVudC1pZCIsInJvbGUiOiJzdHVkZW50IiwiaWF0IjoxNjk1NjM0ODcwLCJleHAiOjE2OTU3MjEyNzB9.Xpo-unj27SwTnfnCUJIPc_BTeK5SPlS8LlDihjv6v6c"))
                println("socket header: $header")
                var options = IO.Options().apply {
                    extraHeaders = header
                    transports = arrayOf(WebSocket.NAME)
                }
                mSocket = IO.socket(uri, options)
                onMessageReceive()
                mSocket.connect()
            } catch (e: Exception) {
                println("socket ${e.message}")
            }
        }

    }

    fun close() {
        mSocket.disconnect()
        mSocket.close()
    }

    fun getSocket(): Socket {
        return mSocket
    }


    private fun onMessageReceive() {
        mSocket.on("message") { args ->
            CoroutineScope(DispatchersIO).launch {
                println("socket message: ${args[0]}")
                try {
                    val message = Json.decodeFromString<MessageFormat>(args[0].toString())

                    repository.insertMessage(
                        message.chattingId,
                        message.message.body,
                        message.message.format,
                        message.message.createdAt,
                        false,
                    )
                } catch (e: Exception) {
                    println("socket message error: ${e.message}")
                }
            }
        }
        mSocket.on("connect") {
            println("socket connect")
        }
        mSocket.on("disconnect") {
            println("socket disconnect ${it}")
        }
    }

    companion object {
        private const val uri = "http://shorttutoring-493721324.ap-northeast-2.elb.amazonaws.com/"
        lateinit var mSocket: Socket
    }
}
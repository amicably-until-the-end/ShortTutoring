package org.softwaremaestro.domain.socket

import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO as DispatchersIO
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.chat.ChatRepository
import javax.inject.Inject


class SocketManager @Inject constructor(private val repository: ChatRepository) {

    private val uri = "http://10.0.2.2:3000"

    val mSocket: Socket = IO.socket(uri)

    fun init() {
        println("socket")

        CoroutineScope(DispatchersIO).launch {
            try {
                mSocket.connect()
            } catch (e: Exception) {
                println("socket ${e.message}")
            }
        }

    }

    private fun onMessageReceive() {
        mSocket.on("message") { args ->

        }
    }
}
package org.softwaremaestro.domain.chat.usecase

import org.softwaremaestro.domain.chat.ChatRepository
import javax.inject.Inject

class InsertMessageUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend fun execute(
        roomId: String,
        body: String,
        format: String,
        sendAt: String,
        isMyMsg: Boolean,
        isRead: Boolean,
    ) =
        repository.insertMessage(roomId, body, format, sendAt, isMyMsg, isRead)
}
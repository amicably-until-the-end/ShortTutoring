package org.softwaremaestro.domain.socket.vo

import kotlinx.serialization.Serializable

@Serializable
data class MessageFormat(
    val chattingId: String,
    val message: Message,
)

@Serializable
data class Message(
    val sender: String,
    val format: String,
    val body: String,
    val createdAt: String

)

package org.softwaremaestro.presenter.chat_page.item

data class ChatRoom(
    val id: String,
    val title: String,
    val subTitle: String,
    val imageUrl: String,
    val contentId: String,
    val roomType: Int,
    val newMessage: Int,
)

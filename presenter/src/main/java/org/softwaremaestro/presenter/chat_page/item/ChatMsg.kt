package org.softwaremaestro.presenter.chat_page.item

data class ChatMsg(
    val id: String,
    val myMsg: Boolean,
    val message: String?,
    val time: String,
    val buttonNumber: Int = 0,
    val buttonString: List<String>?,
    val type: Int,
    val questionDesc: String?,
    val questionImageUrl: String?,
    val questionId: String?,

    )

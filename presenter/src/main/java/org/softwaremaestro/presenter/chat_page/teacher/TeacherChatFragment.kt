package org.softwaremaestro.presenter.chat_page.teacher

import android.widget.Toast
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import org.softwaremaestro.presenter.chat_page.ChatFragment


class TeacherChatFragment : ChatFragment() {
    override fun isTeacher(): Boolean {
        return true
    }


    override fun onChatRoomStateChange(chatRoomVO: ChatRoomVO) {
        TODO("Not yet implemented")
    }


}
package org.softwaremaestro.presenter.chat_page.teacher

import android.widget.Toast
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import org.softwaremaestro.presenter.chat_page.ChatFragment


class TeacherChatFragment : ChatFragment() {
    override fun isTeacher(): Boolean {
        return true
    }

    override fun onChatRightOptionButtonClick() {
        val dialog = TimePickerBottomDialog() {
            Toast.makeText(context, "${it.hour}시 ${it.minute}분", Toast.LENGTH_SHORT).show()
        }.apply {
            setTitle("수업 종료 시간을 선택해주세요")
        }
        dialog.show(parentFragmentManager, "timePicker")
    }

    override fun onChatLeftOptionButtonClick() {
        TODO("Not yet implemented")
    }

    override fun onChatRoomStateChange(chatRoomVO: ChatRoomVO) {
        TODO("Not yet implemented")
    }


}
package org.softwaremaestro.presenter.chat_page.teacher

import android.widget.Toast
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.widget.SimpleDatePicker


class TeacherChatFragment : ChatFragment() {
    override fun isTeacher(): Boolean {
        return true
    }


    override fun onChatRoomStateChange(chatRoomVO: ChatRoomVO) {
        if (chatRoomVO.isSelect) {
            // 지정 질문 이면
            when (chatRoomVO.questionState) {
                QuestionState.PROPOSED -> {
                    enableSelectTimeButton()
                }

                QuestionState.RESERVED -> {
                    enableClassRoomButton()
                }

                else -> {
                }
            }
            enableSelectTimeButton()
        } else {
            //일반 질문 일때
            when (chatRoomVO.questionState) {
                QuestionState.PROPOSED -> {
                    //일반 질문 일 때는 별 다른거 없을 듯
                }

                QuestionState.RESERVED -> {
                    enableClassRoomButton()
                }

                else -> {
                }
            }
        }
    }


    private fun enableSelectTimeButton() {
        binding.btnChatRoomRight.apply {
            text = "수업 시간 선택"
            setEnabledAndChangeColor(true)
            setOnClickListener {
                //TODO : 날짜, 시간 선택하는 다이얼로그 보여주기 픽스하기
            }
        }
    }


}
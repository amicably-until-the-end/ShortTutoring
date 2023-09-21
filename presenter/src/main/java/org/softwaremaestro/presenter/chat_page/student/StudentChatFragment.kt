package org.softwaremaestro.presenter.chat_page.student

import androidx.lifecycle.enableSavedStateHandles
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor


class StudentChatFragment : ChatFragment() {
    override fun isTeacher(): Boolean {
        return false
    }

    override fun onChatRightOptionButtonClick() {
        //TODO("Not yet implemented")
    }

    override fun onChatLeftOptionButtonClick() {
        //TODO("Not yet implemented")
    }

    override fun onChatRoomStateChange(chatRoomVO: ChatRoomVO) {
        if (chatRoomVO.isSelect) {
            //지정 질문 이면
        } else {
            refreshNormalQuestionState(chatRoomVO)
        }
    }

    private fun refreshNormalQuestionState(chatRoomVO: ChatRoomVO) {
        when (chatRoomVO.questionState) {
            QuestionState.PROPOSED -> {
                enableChooseTeacherButton()
            }

            else -> {
                enableClassRoomButton()
            }
        }
    }

    private fun enableClassRoomButton() {
        binding.btnChatRoomRight.apply {
            text = "강의실 입장하기"
            setEnabledAndChangeColor(true)
            setOnClickListener {
                //강의실 입장하기
            }
        }
    }

    private fun enableChooseTeacherButton() {
        binding.btnChatRoomRight.apply {
            text = "선택하기"
            setEnabledAndChangeColor(true)
            setOnClickListener {
                //선생님 선택
            }
        }
    }

}
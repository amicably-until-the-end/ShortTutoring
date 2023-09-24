package org.softwaremaestro.presenter.chat_page.teacher

import android.view.View
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor


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
                    //일반 질문일 때는 별 다른거 없을 듯
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

    override fun enableClassRoomButton() {
        binding.btnChatRoomRight.apply {
            visibility = View.VISIBLE
            setEnabledAndChangeColor(true)
            setOnClickListener {
                enterRoom()
            }
        }
    }

    override fun setChatNoti() {
        binding.cnNoti.apply {
            // Todo: 추후에 시간 변경하기
            setTvNotiMain("학생과의 수업이 7월 77일 7시에 진행됩니다")
            setTvNotiSub("수업을 시작하면 학생이 강의실에 입장할 수 있어요")
            setBtnNegativeText("일정 변경하기")
            setBtnPositiveText("강의실 입장하기")
            setOnClickListenerToBtnNegative {
                visibility = View.GONE
            }
            setOnClickListenerToBtnPositive {
                enterRoom()
            }
        }
    }
}
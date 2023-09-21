package org.softwaremaestro.presenter.chat_page.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.enableSavedStateHandles
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.chat_page.viewmodel.StudentChatViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor


@AndroidEntryPoint
class StudentChatFragment : ChatFragment() {


    private val studentViewModel: StudentChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        setObserver()
    }

    override fun isTeacher(): Boolean {
        return false
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

    private fun setObserver() {
        studentViewModel.pickTeacherResultState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    //로딩
                }

                is UIState.Success -> {
                    enableClassRoomButton()
                }

                is UIState.Failure -> {
                    //선생님 선택 실패
                }

                else -> {}
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
                studentViewModel.pickTeacher(
                    currentChatRoom?.id ?: "",
                    currentChatRoom?.questionId ?: "",
                )
            }
        }
    }

}
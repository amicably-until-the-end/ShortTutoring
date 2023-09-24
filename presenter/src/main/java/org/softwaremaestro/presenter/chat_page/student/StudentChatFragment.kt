package org.softwaremaestro.presenter.chat_page.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.chat_page.viewmodel.StudentChatViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import java.time.LocalDateTime


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
            //TODO : 지정 질문 이면
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
                    with(binding.btnChatRoomRight) {
                        setBackgroundResource(R.drawable.bg_radius_100_background_grey)
                        isEnabled = false
                        setTextColor(resources.getColor(R.color.sub_text_grey, null))
                    }
                }

                is UIState.Success -> {
                    enableClassRoomButton()

                    // 채팅룸의 상태가 변경됐으므로 서버로부터 roomList를 다시 호출
                    getRoomList()
                }

                is UIState.Failure -> {
                    //선생님 선택 실패
                }

                else -> {}
            }
        }
    }


    private fun enableChooseTeacherButton() {
        setNotiVisible(false)
        binding.btnChatRoomRight.apply {
            visibility = View.VISIBLE
            setBackgroundResource(R.drawable.bg_radius_100_grad_blue)
            isEnabled = true
            setTextColor(resources.getColor(R.color.white, null))
            setOnClickListener {
                val dialog = TimePickerBottomDialog { time ->
                    studentViewModel.pickTeacher(
                        LocalDateTime.now().withHour(time.hour).withMinute(time.minute),
                        currentChatRoom?.id ?: "",
                        currentChatRoom?.questionId ?: "",
                    )
                }
                    .apply {
                        setTitle("선생님과 수업을 진행할 시간을 입력해주세요")
                        setBtnText("입력하기")
                    }
                dialog.show(parentFragmentManager, "timePicker")

                // Todo: 추후에 한번 더 확인 받는 방식으로 변경할 수 있음
            }
        }
    }

    override fun enableClassRoomButton() {
        setNotiVisible(true)
        binding.btnChatRoomRight.apply {
            visibility = View.INVISIBLE
        }

        // 공지 띄우고 공지에서 강의실로 이동하기 버튼을 누르는 방식으로 추후에 변경
//        binding.btnChatRoomRight.visibility = View.INVISIBLE
//        enterRoom()
    }

    override fun setChatNoti() {
        binding.cnNoti.apply {
            // Todo: 추후에 시간 변경하기
            setTvNotiMain("선생님과의 수업이 7월 77일 7시에 진행됩니다")
            setTvNotiSub("선생님이 수업을 시작하면 강의실에 입장할 수 있어요")
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
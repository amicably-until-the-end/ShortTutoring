package org.softwaremaestro.presenter.chat_page.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.chat_page.viewmodel.TeacherChatViewModel
import org.softwaremaestro.presenter.util.widget.DatePickerBottomDialog
import org.softwaremaestro.presenter.util.widget.NumberPickerBottomDialog
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import java.time.LocalDateTime


class TeacherChatFragment : ChatFragment() {

    private val teacherViewModel: TeacherChatViewModel by viewModels()
    private lateinit var datePickerDialog: DatePickerBottomDialog
    private lateinit var timePickerDialog: TimePickerBottomDialog
    private lateinit var numberPickerDialog: NumberPickerBottomDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        observeTutoringTimeAndDurationProper()
        initDialog()
        return view
    }

    private fun initDialog() {
        initDatePickerDialog()
        initTimePickerDialog()
        initNumberPickerDialog()
    }

    override fun isTeacher(): Boolean {
        return true
    }

    override fun onChatRoomStateChange(chatRoomVO: ChatRoomVO) {
        if (chatRoomVO.isSelect) {
            // 지정 질문 이면
            when (chatRoomVO.questionState) {
                QuestionState.PROPOSED -> {
                    enableChatRoomBtn()
                }

                QuestionState.RESERVED -> {
                    disableChatRoomBtn()
                }

                else -> {
                }
            }
        } else {
            //일반 질문 일때
            when (chatRoomVO.questionState) {
                QuestionState.PROPOSED -> {
                    //일반 질문일 때는 별 다른거 없을 듯
                }

                QuestionState.RESERVED -> {
                    disableChatRoomBtn()
                }

                else -> {
                }
            }
        }
    }


    override fun enableChatRoomBtn() {
        setNotiVisible(false)
        binding.btnChatRoomRight.apply {
            visibility = View.VISIBLE
            text = "수락하기"
            setBackgroundResource(R.drawable.bg_radius_100_grad_blue)
            this.isEnabled = true
            setTextColor(resources.getColor(R.color.white, null))
            setOnClickListener {
                datePickerDialog.show(parentFragmentManager, "datePicker")
            }
        }

        binding.btnChatRoomLeft.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                // 지정 질문 거절하기
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

    private fun observeTutoringTimeAndDurationProper() {
        teacherViewModel.tutoringTimeAndDurationProper.observe(viewLifecycleOwner) {
            if (it) teacherViewModel.pickStudent()
        }
    }

    private fun initDatePickerDialog() {
        datePickerDialog = DatePickerBottomDialog { date ->
            with(date) {
                teacherViewModel.setTutoringTime(
                    LocalDateTime.now()
                        .withYear(year)
                        .withMonth(monthValue)
                        .withDayOfMonth(dayOfMonth)
                )
            }
            timePickerDialog.show(parentFragmentManager, "datePicker")
        }.apply {
            setTitle("수업 날짜를 선택해주세요")
            setBtnText("선택하기")
        }
    }

    private fun initTimePickerDialog() {
        timePickerDialog = TimePickerBottomDialog { time ->
            teacherViewModel.setTutoringTime(
                with(teacherViewModel.tutoringTime.value!!) {
                    LocalDateTime.of(
                        year,
                        monthValue,
                        dayOfMonth,
                        time.hour,
                        time.minute
                    )
                }
            )
            numberPickerDialog.show(parentFragmentManager, "numberPicker")
        }.apply {
            setTitle("수업 시작 시간을 선택해주세요")
            setBtnText("선택하기")
        }
    }

    private fun initNumberPickerDialog() {
        numberPickerDialog = NumberPickerBottomDialog { number ->
            teacherViewModel.setTutoringDuration(number)
        }.apply {
            setTitle("수업을 몇 분간 진행할까요?")
            setBtnText("입력하기")
        }
    }
}
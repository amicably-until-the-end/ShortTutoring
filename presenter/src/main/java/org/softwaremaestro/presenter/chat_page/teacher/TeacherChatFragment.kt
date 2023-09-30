package org.softwaremaestro.presenter.chat_page.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.chat_page.viewmodel.TeacherChatViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.widget.DatePickerBottomDialog
import org.softwaremaestro.presenter.util.widget.NumberPickerBottomDialog
import org.softwaremaestro.presenter.util.widget.SimpleConfirmDialog
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
        observe()
        initDialog()
        return view
    }

    private fun observe() {
        observeTutoringTimeAndDurationProper()
        observePickStudentResult()
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
                    onReservedRoomSelect()
                }

                else -> {
                }
            }
        } else {
            //일반 질문 일때
            when (chatRoomVO.questionState) {
                QuestionState.PROPOSED -> {
                    setChatRoomBtnsVisible(false)
                    setNotiVisible(false)
                }

                QuestionState.RESERVED -> {
                    onReservedRoomSelect()
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

    fun observeClassroomInfo() {
        chatViewModel.classroomInfo.observe(viewLifecycleOwner) {

            when (it) {
                is UIState.Empty -> return@observe
                is UIState.Loading -> {
                    loadingDialog.show()
                }

                is UIState.Success -> {
                    loadingDialog.dismiss()
                    if (!it._data?.boardAppId.isNullOrEmpty()) {
                        SimpleConfirmDialog {
                            moveToClassRoom(it._data!!)
                        }.show(parentFragmentManager, "enterClassroomDialog")
                    } else {
                        Toast.makeText(requireContext(), "강의실 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is UIState.Failure -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), "강의실 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {

                }

            }
            chatViewModel._tutoringInfo.value = UIState.Empty
            //액티비티 종료되어 돌아오는 경우에 대비해서 초기화
        }
    }

    fun setChatNoti() {
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
                getClassRoomInfo()
            }
        }
    }

    private fun observeTutoringTimeAndDurationProper() {
        teacherViewModel.tutoringTimeAndDurationProper.observe(viewLifecycleOwner) { proper ->
            if (proper) {
                currentChatRoom?.let {
                    teacherViewModel.pickStudent(it.questionId!!, it.id!!)
                }
            }
        }
    }

    private fun observePickStudentResult() {
        teacherViewModel.pickStudentResult.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    //로딩
                    listOf(
                        binding.btnChatRoomRight,
                        binding.btnChatRoomLeft
                    ).forEach { btn ->
                        btn.setBackgroundResource(R.drawable.bg_radius_100_background_grey)
                        btn.isEnabled = false
                        btn.setTextColor(resources.getColor(R.color.sub_text_grey, null))
                    }
                }

                is UIState.Success -> {
                    // 채팅룸의 상태가 변경됐으므로 서버로부터 roomList를 다시 호출
                    chatViewModel.getChatRoomList(isTeacher())
                }

                is UIState.Failure -> {
                    //선생님 선택 실패
                }

                else -> {}
            }
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

    private fun onReservedRoomSelect() {
        disableChatRoomBtn()
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
package org.softwaremaestro.presenter.chat_page.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.chat_page.viewmodel.StudentChatViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.widget.DatePickerBottomDialog
import org.softwaremaestro.presenter.util.widget.NumberPickerBottomDialog
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import java.time.LocalDateTime


@AndroidEntryPoint
class StudentChatFragment : ChatFragment() {

    private val studentViewModel: StudentChatViewModel by viewModels()
    private lateinit var datePickerDialog: DatePickerBottomDialog
    private lateinit var timePickerDialog: TimePickerBottomDialog
    private lateinit var numberPickerDialog: NumberPickerBottomDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        observePickTeacherResultState()
        observeTutoringTimeAndDurationProper()
        initDialog()
        return view
    }

    override fun isTeacher(): Boolean {
        return false
    }

    override fun onChatRoomStateChange(chatRoomVO: ChatRoomVO) {
        if (chatRoomVO.isSelect) {
            when (chatRoomVO.questionState) {
                QuestionState.PROPOSED -> {
                    onProposedSelectQuestionSelect()
                }

                else -> {
                    onReservedRoomSelect()
                }
            }
        } else {
            when (chatRoomVO.questionState) {
                QuestionState.PROPOSED -> {
                    onProposedNormalQuestionSelect()
                }

                else -> {
                    onReservedRoomSelect()
                }
            }
        }
    }

    private fun onProposedSelectQuestionSelect() {
        hideLeftButton()
        hideRightButton()
    }

    private fun hideLeftButton() {
        binding.btnChatRoomLeft.visibility = View.GONE
    }

    private fun hideRightButton() {
        binding.btnChatRoomRight.visibility = View.GONE
    }

    override fun observeTutoringInfo() {
        chatViewModel.tutoringInfo.observe(viewLifecycleOwner) {

            when (it) {
                is UIState.Loading -> {
                    loadingDialog.show()
                }

                is UIState.Success -> {
                    Log.d("tutoring", it._data.toString())
                    loadingDialog.dismiss()
                    if (!it._data?.whiteBoardAppId.isNullOrEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "강의실에 입장합니다. ${it._data?.whiteBoardAppId}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        moveToClassRoom(it._data!!)
                        chatViewModel._tutoringInfo.value = UIState.Empty
                    } else {
                        Toast.makeText(requireContext(), "아직 수업 시작 전입니다.", Toast.LENGTH_SHORT)
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

    private fun observePickTeacherResultState() {
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
                    disableChatRoomBtn()

                    // Todo: 다른 선생님에게 거절 문자하기

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

    private fun onProposedNormalQuestionSelect() {
        setNotiVisible(false)
        enablePickTeacherButton()
    }

    private fun onReservedRoomSelect() {
        setNotiVisible(true)
//        enableEnterClassroomBtn()
        observeTutoringInfo()
    }

    private fun enableEnterClassroomBtn() {
        binding.btnChatRoomRight.apply {
            visibility = View.VISIBLE
            text = "강의실 입장하기"
            setBackgroundResource(R.drawable.bg_radius_100_grad_blue)
            isEnabled = true
            setTextColor(resources.getColor(R.color.white, null))
            setOnClickListener {
                currentChatRoom?.let {
                    chatViewModel.getClassRoomInfo(it.id!!)
                }
            }
        }
    }

    private fun initDatePickerDialog() {
        datePickerDialog = DatePickerBottomDialog { date ->
            with(date) {
                studentViewModel.setTutoringTime(
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

    private fun enablePickTeacherButton() {

        binding.btnChatRoomRight.apply {
            visibility = View.VISIBLE
            setBackgroundResource(R.drawable.bg_radius_100_grad_blue)
            isEnabled = true
            setTextColor(resources.getColor(R.color.white, null))
            setOnClickListener {
                datePickerDialog.show(parentFragmentManager, "datePicker")
            }
        }
    }

    private fun initDialog() {
        initDatePickerDialog()
        initTimePickerDialog()
        initNumberPickerDialog()
    }

    private fun initTimePickerDialog() {
        timePickerDialog = TimePickerBottomDialog { time ->
            studentViewModel.setTutoringTime(
                with(studentViewModel.tutoringTime.value!!) {
                    java.time.LocalDateTime.of(
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
            studentViewModel.setTutoringDuration(number)
        }.apply {
            setTitle("수업을 몇 분간 진행할까요?")
            setBtnText("입력하기")
        }
    }

    private fun observeTutoringTimeAndDurationProper() {
        studentViewModel.tutoringTimeAndDurationProper.observe(viewLifecycleOwner) { proper ->
            if (proper) {
                currentChatRoom?.let {
                    studentViewModel.pickTeacher(it.id!!, it.questionId!!)
                }
                studentViewModel.setTutoringTime(null)
                studentViewModel.setTutoringDuration(null)
            }
        }
    }

    override fun enableChatRoomBtn() {
        return
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
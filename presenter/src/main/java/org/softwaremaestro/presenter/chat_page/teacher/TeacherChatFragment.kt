package org.softwaremaestro.presenter.chat_page.teacher

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.softwaremaestro.domain.chat.entity.ChatRoomState
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.chat_page.ChatFragment
import org.softwaremaestro.presenter.chat_page.viewmodel.TeacherChatViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.toKoreanString
import org.softwaremaestro.presenter.util.widget.DatePickerBottomDialog
import org.softwaremaestro.presenter.util.widget.NumberPickerBottomDialog
import org.softwaremaestro.presenter.util.widget.SimpleConfirmDialog
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import java.time.LocalDateTime
import java.time.LocalTime


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
        observePickStudentResult()
        observeClassroomInfo()
        observeTutoringInfo()
    }


    private fun observeTutoringInfo() {
        chatViewModel.tutoringInfo.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Empty -> return@observe

                is UIState.Success -> {
                    it._data?.let { tutoringInfo ->
                        setChatNoti(tutoringInfo.reservedStart, tutoringInfo.id)
                        tutoringId = tutoringInfo.id
                        if (tutoringInfo.status == "finished") {
                            setNotiVisible(false)
                            enableExitRoomBtn()
                        }
                    }
                }

                is UIState.Failure -> {
                    setChatNoti(null, null)
                }

                else -> {}
            }

        }
    }

    private fun initDialog() {
        initDatePickerDialog()
        initTimePickerDialog()
        initNumberPickerDialog()
    }

    override fun isTeacher(): Boolean {
        return true
    }

    override fun pickTeacher(startTime: LocalDateTime, endTime: LocalDateTime) {
        return
    }


    override fun onChatRoomStateChange(chatRoomVO: ChatRoomVO) {
        enableChatting(true)
        if (chatRoomVO.isSelect) {
            // 지정 질문 이면
            when (chatRoomVO.chatRoomState) {
                ChatRoomState.PROPOSED -> {
                    onProposedSelectRoomEnter()
                }

                ChatRoomState.RESERVED -> {
                    onReservedRoomEnter()
                }

                ChatRoomState.DECLINED -> {
                    onDeclinedRoomEnter()
                    //선생님 자신이 지정 질문 거절한 경우
                }


                else -> {
                    Log.e("${this@TeacherChatFragment::class.java}", "invalid question state")
                }
            }
        } else {
            //일반 질문 일때
            when (chatRoomVO.chatRoomState) {
                ChatRoomState.PROPOSED -> {
                    onProposedNormalRoomEnter()
                }

                ChatRoomState.RESERVED -> {
                    onReservedRoomEnter()
                }

                ChatRoomState.DECLINED -> {
                    //학생이 일반 질문 거절한 경우
                    onDeclinedRoomEnter()
                }

                else -> {
                    Log.e("${this@TeacherChatFragment::class.java}", "invalid ChatRoomState")
                }
            }
        }
    }

    private fun onProposedNormalRoomEnter() {
        setNotiVisible(false)
        enableOfferSchedule()
        enableSendMessage()
    }


    override fun enablePickStudentBtn() {
        //setNotiVisible(false)
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
    }

    private fun onDeclinedRoomEnter() {
        setNotiVisible(false)
        setChatRoomRightBtnVisible(false)
        binding.btnChatRoomLeft.visibility = View.GONE
        disableSendMessage()
        enableExitRoomBtn()
    }

    private fun onProposedSelectRoomEnter() {
        setNotiVisible(false)
        enableSendMessage()
        enableOfferSchedule()
        enablePickStudentBtn()
        enableDeclineBtn()
    }

    private fun enableDeclineBtn() {
        binding.btnChatRoomLeft.apply {
            visibility = View.VISIBLE
            text = "거절하기"
            setBackgroundResource(R.drawable.bg_radius_100_grad_blue)
            this.isEnabled = true
            setTextColor(resources.getColor(R.color.white))
            setOnClickListener {
                SimpleConfirmDialog {
                    currentChatRoom?.id?.let { teacherViewModel.declineQuestion(it) }
                }.apply {
                    title = "거절하시겠습니까?"
                    description = "학생이 요청한 시간에 진행이 어렵다면 채팅을 통해 조율해보세요."
                }.show(parentFragmentManager, "declineDialog")
            }
        }
    }

    private fun onReservedRoomEnter() {
        currentChatRoom?.questionId?.let { chatViewModel.getTutoringInfo(it) }
        setNotiVisible(true)
        binding.btnChatRoomRight.visibility = View.GONE
        binding.btnChatRoomLeft.visibility = View.GONE
        enableSendMessage()
    }

    private fun observeClassroomInfo() {
        chatViewModel.classroomInfo.observe(viewLifecycleOwner) {
            Log.d("observeClassroomInfo", "observeClassroomInfo: ${it}")
            when (it) {
                is UIState.Empty -> return@observe
                is UIState.Loading -> {
                    loadingDialog.show()
                }

                is UIState.Success -> {
                    loadingDialog.dismiss()
                    if (!it._data?.boardAppId.isNullOrEmpty()) {
                        moveToClassRoom(it._data!!)
                    } else {
                        Util.createToast(
                            requireActivity(),
                            "강의실 정보를 가져오지 못했습니다."
                        )
                            .show()
                    }
                    chatViewModel._classroomInfo.value = UIState.Empty
                }

                is UIState.Failure -> {
                    loadingDialog.dismiss()
                    Util.createToast(requireActivity(), "강의실 정보를 가져오지 못했습니다.")
                        .show()
                    chatViewModel._classroomInfo.value = UIState.Empty
                }

                else -> {

                }

            }

            //액티비티 종료되어 돌아오는 경우에 대비해서 초기화
        }
    }

    fun setChatNoti(startAt: LocalDateTime?, tutoringId: String?) {
        Log.d("setChatNoti", "setChatNoti: ${startAt} ${tutoringId} ")
        binding.cnNoti.apply {
            setTvNotiMain("수업이 ${startAt?.toKoreanString()}에 예약되어있습니다.")
            setTvNotiSub("수업을 시작하면 학생이 강의실에 입장할 수 있어요")
            setBtnNegativeText("닫기")
            setBtnPositiveText("강의실 입장하기")
            setOnClickListenerToBtnNegative {
                setNotiVisible(false)
            }
            setOnClickListenerToBtnPositive {
                if (chatViewModel.tutoringInfo.value?._data?.status == "finished") {
                    Util.createToast(requireActivity(), "이미 종료된 수업입니다.").show()
                    return@setOnClickListenerToBtnPositive
                }
                SimpleConfirmDialog {
                    tutoringId?.let { chatViewModel.startClassroom(it) }
                }.apply {
                    title = "강의실에 입장합니다"
                    description = "학생에게 수업 시작을 알립니다.\n학생이 들어오면 수업을 시작해주세요!"
                }.show(parentFragmentManager, "enterClassroomDialog")
            }
        }
    }

    private fun observePickStudentResult() {
        teacherViewModel.pickStudentResult.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Empty -> return@observe
                is UIState.Loading -> {
                    //로딩
                    loadingDialog.show()
                    with(binding.btnChatRoomRight) {
                        isEnabled = false
                        setTextColor(resources.getColor(R.color.sub_text_grey, null))
                    }
                }

                is UIState.Success -> {
                    disableChatRoomBtn()
                    loadingDialog.dismiss()
                    binding.btnChatRoomRight.isEnabled = false
                    // 채팅룸의 상태가 변경됐으므로 서버로부터 roomList를 다시 호출
                    //chatViewModel.getChatRoomList(isTeacher())
                }

                is UIState.Failure -> {
                    loadingDialog.dismiss()
                    with(binding.btnChatRoomRight) {
                        isEnabled = true
                        setTextColor(resources.getColor(R.color.white, null))
                    }
                }

                else -> {}
            }
            teacherViewModel._pickStudentResult.value = UIState.Empty
        }
    }

    private fun initDatePickerDialog() {
        datePickerDialog = DatePickerBottomDialog { date ->
            teacherViewModel.tutoringDate = date
            timePickerDialog.show(parentFragmentManager, "timePicker")
        }.apply {
            setTitle("수업 날짜를 선택해주세요")
            setBtnText("선택하기")
        }
    }

    private fun initTimePickerDialog() {
        timePickerDialog = TimePickerBottomDialog { time ->
            teacherViewModel.tutoringStart = LocalTime.of(time.hour, time.minute)
            numberPickerDialog.show(parentFragmentManager, "numberPicker")
        }.apply {
            setTitle("수업 시작 시간을 선택해주세요")
            setBtnText("선택하기")
        }
    }

    private fun initNumberPickerDialog() {
        numberPickerDialog = NumberPickerBottomDialog { number ->
            teacherViewModel.tutoringDuration = number
            teacherViewModel.offerSchedule(currentChatRoom?.questionId!!, currentChatRoom?.id!!)
        }.apply {
            setTitle("수업을 몇 분간 진행할까요?")
            setBtnText("입력하기")
        }
    }

    private fun enableOfferSchedule() {
        binding.btnChatRoomLeft.visibility = View.GONE
        binding.btnChatRoomRight.apply {
            visibility = View.VISIBLE
            text = "시간 정하기"
            setTextColor(resources.getColor(R.color.white, null))
            setBackgroundResource(R.drawable.bg_radius_100_grad_blue)
            isEnabled = true
            setOnClickListener {
                datePickerDialog.show(parentFragmentManager, "datePicker")
            }
        }
    }
}

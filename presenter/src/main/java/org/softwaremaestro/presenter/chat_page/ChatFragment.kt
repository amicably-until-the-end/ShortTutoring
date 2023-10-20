package org.softwaremaestro.presenter.chat_page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.chat.entity.QuestionState
import org.softwaremaestro.domain.chat.entity.RoomType
import org.softwaremaestro.domain.classroom.entity.ClassroomInfoVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.chat_page.QuestionImageActivity.Companion.QUESTION_ID
import org.softwaremaestro.presenter.chat_page.adapter.ChatRoomIconListAdapter
import org.softwaremaestro.presenter.chat_page.adapter.ChatRoomListAdapter
import org.softwaremaestro.presenter.chat_page.adapter.MessageListAdapter
import org.softwaremaestro.presenter.chat_page.viewmodel.ChatViewModel
import org.softwaremaestro.presenter.chat_page.widget.AnsweringTeacherSelectDialog
import org.softwaremaestro.presenter.classroom.ClassroomActivity
import org.softwaremaestro.presenter.classroom.ClassroomFragment
import org.softwaremaestro.presenter.classroom.ReviewDialog
import org.softwaremaestro.presenter.classroom.item.SerializedVoiceRoomInfo
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.databinding.FragmentChatPageBinding
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.CLASSROOM_END_RESULT
import org.softwaremaestro.presenter.student_home.viewmodel.HomeViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.ReviewViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.getVerticalSpaceDecoration
import org.softwaremaestro.presenter.util.hideKeyboardAndRemoveFocus
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.SimpleConfirmDialog
import javax.inject.Inject


@AndroidEntryPoint
abstract class ChatFragment : Fragment() {

    protected lateinit var binding: FragmentChatPageBinding
    private lateinit var proposedAdapter: ChatRoomListAdapter
    private lateinit var reservedAdapter: ChatRoomListAdapter
    private lateinit var messageListAdapter: MessageListAdapter
    private lateinit var offeringTeacherAdapter: ChatRoomListAdapter
    private lateinit var proposedIconAdapter: ChatRoomIconListAdapter

    private var selectedTutoringIndex: Int? = null
    private var selectedTeacher: Int? = null

    private var recyclerViewAdapters: MutableList<RecyclerView.Adapter<*>> = mutableListOf()

    protected val chatViewModel: ChatViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val reviewViewModel: ReviewViewModel by activityViewModels()

    protected var currentChatRoom: ChatRoomVO? = null

    lateinit var loadingDialog: LoadingDialog

    private var isNormalQuestionTab: Boolean = true

    @Inject
    lateinit var socketManager: SocketManager

    protected lateinit var tutoringId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatPageBinding.inflate(inflater, container, false)

        setProposedRecyclerView()
        setReservedRecyclerView()
        setChatMsgRecyclerView()
        setQuestionTypeSelectToggle()
        setApplyIconRecyclerView()
        setOfferingTeacherRecyclerView()
        setCloseOfferingTeacherButton()
        observeChatRoomList()
        makeAdapterList()
        setSendMessageButton()
        observeMessages()
        observeSocket()
        observeCurrentRoom()
        clearChatRoomState()
        observeExitRoomResult()

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireContext())
    }

    override fun onStart() {
        super.onStart()
        getRoomList()
    }


    private fun setSendMessageButton() {
        binding.btnSendMessage.setOnClickListener {
            sendMessage()
            binding.etMessage.setText("")
        }
    }

    private fun getRoomList() {
        chatViewModel.getChatRoomList(isTeacher(), currentChatRoom?.id)
    }

    override fun onStop() {
        super.onStop()
        socketManager.setChatRoomId(null)
    }

    /**
     * 현재 보고있는 채팅방의 정보가 바뀌었을때 UI를 새로고침한다.
     */
    private fun observeCurrentRoom() {
        chatViewModel._currentChattingRoomVO.postValue(null)
        chatViewModel.currentChattingRoomVO.observe(viewLifecycleOwner) {
            it?.let {
                enterChatRoom(it)
            }
        }
    }

    private fun checkDeepLinkArgs() {
        if (!homeViewModel.chattingId.isNullOrEmpty()) {
            focusChatRoom(homeViewModel.chattingId!!)
        }
    }

    private fun clearChatRoomState() {
        currentChatRoom = null
        setMessageInputBoxVisibility()
        binding.tvChatRoomTitle.text = ""
        binding.btnChatRoomRight.visibility = View.GONE
        binding.btnChatRoomLeft.visibility = View.GONE
        binding.cnNoti.visibility = View.GONE
        messageListAdapter.setItem(emptyList())
    }

    private fun observeSocket() {
        //viewmodel로 뺄 수 도 있겠다.
        socketManager.addOnMessageReceiveListener { chatRoomId ->
            if (currentChatRoom?.id == chatRoomId) {
                chatViewModel.getMessages(chatRoomId)
            }
            getRoomList()
        }
    }

    override fun onPause() {
        super.onPause()
        chatViewModel._currentChattingRoomVO.postValue(null)
        currentChatRoom = null
    }

    private fun observeChatRoomChange() {
        //TODO: 채팅방 전체 갱신 안하고 하나씩 새로고침하는 리팩토링 용도
        chatViewModel.changedChatRoom.observe(viewLifecycleOwner) {
            it?.apply {
            }
        }
    }

    private fun sendMessage() {
        if (binding.etMessage.text.isNullOrEmpty()) return
        currentChatRoom?.let {
            chatViewModel.sendMessage(
                binding.etMessage.text.toString(),
                chattingId = it.id,
                receiverId = it.opponentId!!,
            )
        }
    }

    fun disableSendMessage() {
        binding.btnSendMessage.isEnabled = false
        binding.containerInputBox.alpha = 0.5f
        binding.etMessage.hint = "거절되었습니다."
        binding.etMessage.isEnabled = false
    }

    fun enableSendMessage() {
        binding.btnSendMessage.isEnabled = true
        binding.containerInputBox.alpha = 1f
        binding.etMessage.hint = "메시지를 입력하세요"
        binding.etMessage.isEnabled = true
    }

    private fun refreshProposedRoomList() {
        if (isNormalQuestionTab) {
            chatViewModel.proposedNormalChatRoomList.value?.let {
                when (chatViewModel.proposedNormalChatRoomList.value) {
                    is UIState.Success -> {
                        setProposedSectionItems(it._data!!)
                    }

                    else -> {}
                }
            }

        } else {
            chatViewModel.proposedSelectedChatRoomList.value?.let {
                when (chatViewModel.proposedSelectedChatRoomList.value) {
                    is UIState.Success -> {
                        setProposedSectionItems(it._data!!)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun observeMessages() {
        chatViewModel.messages.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Empty -> return@observe
                is UIState.Success -> {
                    setMessageListItems(it._data!!)
                }

                else -> {

                }

            }
            chatViewModel.setMessageUI(UIState.Empty)
        }
    }

    abstract fun enablePickStudentBtn()

    protected fun disableChatRoomBtn() {
        //setNotiVisible(true)
        setChatRoomBtnsVisible(false)
    }

    fun getClassRoomInfo() {
        if (currentChatRoom?.questionId == null) {
            Toast.makeText(requireContext(), "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        chatViewModel.getClassroomInfo(currentChatRoom?.questionId!!)
    }


    private fun refreshReservedRoomList() {
        if (isNormalQuestionTab) {
            chatViewModel.reservedNormalChatRoomList.value?.let {
                when (chatViewModel.reservedNormalChatRoomList.value) {
                    is UIState.Success -> {
                        setReservedSectionItems(it._data!!)
                    }

                    else -> {}
                }
            }

        } else {
            chatViewModel.reservedSelectedChatRoomList.value?.let {
                when (chatViewModel.reservedSelectedChatRoomList.value) {
                    is UIState.Success -> {
                        setReservedSectionItems(it._data!!)
                    }

                    else -> {}
                }
            }
        }
    }

    abstract fun isTeacher(): Boolean

    /**
     * 변화되는 채팅 목록을 observe
     */
    private fun observeChatRoomList() {

        chatViewModel.proposedSelectedChatRoomList.observe(viewLifecycleOwner) {
            refreshProposedRoomList()
            checkDeepLinkArgs()
            refreshTabBadge()
        }
        chatViewModel.proposedNormalChatRoomList.observe(viewLifecycleOwner) {
            refreshProposedRoomList()
            if (!isTeacher()) {
                // 학생일 경우에만 왼쪽 아이콘 뷰 갱신
                setProposedIconItems(it._data ?: emptyList())
                proposedIconAdapter.selectedQuestionId?.let {
                    chatViewModel.proposedNormalChatRoomList.value?._data?.find { room ->
                        room.questionId == it
                    }?.let { room ->
                        setOfferingTeacherListItems(room.teachers ?: emptyList())
                        offeringTeacherAdapter.notifyDataSetChanged()
                    }
                }
            }
            checkDeepLinkArgs()
            refreshTabBadge()
        }
        chatViewModel.reservedSelectedChatRoomList.observe(viewLifecycleOwner) {
            refreshReservedRoomList()
            checkDeepLinkArgs()
            refreshTabBadge()

        }
        chatViewModel.reservedNormalChatRoomList.observe(viewLifecycleOwner) {
            refreshReservedRoomList()
            checkDeepLinkArgs()
            refreshTabBadge()

        }
    }

    private fun refreshTabBadge() {
        val normalRooms = listOf(
            chatViewModel.reservedNormalChatRoomList.value?._data,
            chatViewModel.proposedNormalChatRoomList.value?._data,
        )
        val selectedRooms = listOf(
            chatViewModel.reservedSelectedChatRoomList.value?._data,
            chatViewModel.proposedSelectedChatRoomList.value?._data,
        )
        binding.tvNormalBadge.visibility = View.GONE
        binding.tvSelectedBadge.visibility = View.GONE
        normalRooms.forEach { rooms ->
            rooms?.find { (it.messages ?: 0) > 0 }?.let {
                binding.tvNormalBadge.visibility = View.VISIBLE
                return@forEach
            }
        }
        selectedRooms.forEach { rooms ->
            rooms?.find { (it.messages ?: 0) > 0 }?.let {
                binding.tvSelectedBadge.visibility = View.VISIBLE
                return@forEach
            }
        }
    }


    /**
     * 모든 어댑터를 초기화 한 이후에 실행해야 함
     */
    private fun makeAdapterList() {
        recyclerViewAdapters.apply {
            add(reservedAdapter)
            add(proposedAdapter)
            add(messageListAdapter)
            add(offeringTeacherAdapter)
            add(proposedIconAdapter)
        }
    }

    private fun setCloseOfferingTeacherButton() {
        binding.btnCloseOfferingTeacher.setOnClickListener {
            chatViewModel.getChatRoomList(isTeacher(), currentChatRoom?.id)
            unSetOfferingTeacherMode()
        }
    }

    private fun setProposedRecyclerView() {

        proposedAdapter = ChatRoomListAdapter(
            onQuestionClick = onQuestionRoomClick,
            onTeacherClick = onTeacherRoomClick,
        )
        binding.rvApplyList.apply {
            adapter = proposedAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(getVerticalSpaceDecoration(10, requireContext()))
        }
        binding.tvApplyCount.text = proposedAdapter.itemCount.toString()
    }


    private fun setSelectedRoomId(selectedRoomId: String?) {

        recyclerViewAdapters.listIterator().forEach {
            when (it) {
                is ChatRoomListAdapter -> {
                    it.setSelectedChattingRoomId(selectedRoomId)
                }

                else -> {}
            }
        }

    }

    private fun setQuestionTypeSelectToggle() {
        binding.rbSelectedQuestion.setOnClickListener {
            setSelectedRoomId(null)
            clearChatRoomState()
            setRoomListToSelected()
            isNormalQuestionTab = false
            it.background = requireContext().getDrawable(R.drawable.bg_radius_5_white)
            binding.rbNormalQuestion.background =
                requireContext().getDrawable(R.drawable.bg_radius_10_transparent)
        }
        binding.rbNormalQuestion.setOnClickListener {
            setSelectedRoomId(null)
            clearChatRoomState()
            setRoomListToNormal()
            isNormalQuestionTab = true
            it.background = requireContext().getDrawable(R.drawable.bg_radius_5_white)
            binding.rbSelectedQuestion.background =
                requireContext().getDrawable(R.drawable.bg_radius_10_transparent)
        }
    }

    private fun toggleQuestionType(isSelect: Boolean) {
        if (isSelect) {
            isNormalQuestionTab = false
            binding.rbNormalQuestion.background =
                requireContext().getDrawable(R.drawable.bg_radius_10_transparent)
            binding.rbSelectedQuestion.background =
                requireContext().getDrawable(R.drawable.bg_radius_5_white)
            setRoomListToSelected()
        } else {
            isNormalQuestionTab = true
            binding.rbNormalQuestion.background =
                requireContext().getDrawable(R.drawable.bg_radius_5_white)
            binding.rbSelectedQuestion.background =
                requireContext().getDrawable(R.drawable.bg_radius_10_transparent)
            setRoomListToNormal()
        }
    }

    private fun setRoomListToNormal() {
        setReservedSectionItems(
            chatViewModel.reservedNormalChatRoomList.value?._data ?: emptyList()
        )
        setProposedSectionItems(
            chatViewModel.proposedNormalChatRoomList.value?._data ?: emptyList()
        )
    }

    private fun setRoomListToSelected() {
        setReservedSectionItems(
            chatViewModel.reservedSelectedChatRoomList.value?._data ?: emptyList()
        )
        setProposedSectionItems(
            chatViewModel.proposedSelectedChatRoomList.value?._data ?: emptyList()
        )
    }

    /**
     * chattingId를 가진 방으로 UI를 전환한다.
     */

    private fun focusChatRoom(chattingId: String) {
        val liveDatas = mutableListOf(
            chatViewModel.reservedSelectedChatRoomList,
            chatViewModel.reservedNormalChatRoomList,
            chatViewModel.proposedSelectedChatRoomList,
            chatViewModel.proposedNormalChatRoomList,
        )
        val list = liveDatas.mapNotNull { it.value?._data }.toMutableList()

        chatViewModel.proposedNormalChatRoomList.value?._data?.forEach {
            it.teachers?.forEach { room ->
                list.add(listOf(room))
            }
        }
        list.forEach { rooms ->

            rooms.find { it.id == chattingId }?.let {
                Log.d("checkDeepLinkArgs", "find ${it}")
                if (it.isSelect) {
                    toggleQuestionType(true)
                } else {
                    toggleQuestionType(false)
                }
                if (!isTeacher() && !it.isSelect && it.questionState == QuestionState.PROPOSED) {
                    val teachers =
                        chatViewModel.proposedNormalChatRoomList.value?._data?.find { room ->
                            room.questionId == it.questionId
                        }?.teachers
                    onQuestionRoomClick(teachers ?: emptyList(), it.questionId, proposedAdapter)
                    setOfferingTeacherMode()
                } else {
                    unSetOfferingTeacherMode()
                }
                if (it.roomType == RoomType.TEACHER) {
                    enterChatRoom(it)
                }
                homeViewModel.chattingId = null
                return
            }
        }
    }

    private fun resetMsgTab() {
        //setNotiVisible(false)
        setChatRoomBtnsVisible(false)
        messageListAdapter.setItem(emptyList())
        messageListAdapter.notifyDataSetChanged()
        binding.tvChatRoomTitle.text = ""
        binding.etMessage.setText("")
        hideKeyboardAndRemoveFocus(binding.etMessage)
    }

    abstract fun onChatRoomStateChange(chatRoomVO: ChatRoomVO)

    private fun setReservedSectionItems(list: List<ChatRoomVO>) {
        binding.apply {
            tvReservedCount.text = list.size.toString()
            cvQuestionReservedEmpty.visibility = if (list.isNotEmpty()) View.GONE else View.VISIBLE
            reservedAdapter.setItem(list.map { it.id })
            reservedAdapter.setRoomInfo(chatRoomVOtoMap(list))
            reservedAdapter.notifyDataSetChanged()
        }
    }

    private fun setProposedSectionItems(list: List<ChatRoomVO>) {
        binding.apply {
            tvApplyCount.text = list.size.toString()
            cvQuestionProposedEmpty.visibility = if (list.isNotEmpty()) View.GONE else View.VISIBLE
            proposedAdapter.setItem(list.map { it.id })
            proposedAdapter.setRoomInfo(chatRoomVOtoMap(list))
            proposedAdapter.notifyDataSetChanged()
        }
    }

    private fun setReservedRecyclerView() {
        reservedAdapter = ChatRoomListAdapter(
            onQuestionClick = onQuestionRoomClick,
            onTeacherClick = onTeacherRoomClick,
        )
        binding.rvReservedList.apply {
            adapter = reservedAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(getVerticalSpaceDecoration(10, requireContext()))
        }
        binding.tvReservedCount.text = reservedAdapter.itemCount.toString()
    }

    private fun setChatMsgRecyclerView() {
        messageListAdapter = MessageListAdapter(
            isTeacher(),
            onBtn1Click = {
                val dialog = AnsweringTeacherSelectDialog(currentChatRoom?.id!!)
                dialog.show(parentFragmentManager, "dialog")
            },
            onBtn2Click = {
                SimpleConfirmDialog {
                    //TODO: 질문 삭제 API 호출
                }.apply {
                    title = "질문을 삭제할까요?"
                    description = "작성한 질문 내용이 사라집니다"
                }.show(
                    parentFragmentManager,
                    "detailAlertDialog"
                )
            },
            onImageClick = {
                val intent = Intent(requireActivity(), QuestionImageActivity::class.java).apply {
                    putExtra(QUESTION_ID, currentChatRoom?.questionId)
                }
                startActivity(intent)
            }
        )
        binding.rvMsgs.apply {
            adapter = messageListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(getVerticalSpaceDecoration(15, requireContext()))
        }
    }

    private fun scrollMessageToBottom() {
        binding.rvMsgs.scrollToPosition(messageListAdapter.itemCount - 1)
    }

    private fun setMessageListItems(messages: List<MessageVO>) {
        messageListAdapter.setItem(
            messages
        )
        binding.rvMsgs.scrollToPosition(messages.size - 1)
        messageListAdapter.notifyDataSetChanged()
        scrollMessageToBottom()
    }

    private fun setOfferingTeacherRecyclerView() {
        binding.rvOfferingTeacherList.apply {
            offeringTeacherAdapter = ChatRoomListAdapter(
                onQuestionClick = onQuestionRoomClick,
                onTeacherClick = onTeacherRoomClick,
            )
            adapter = offeringTeacherAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(getVerticalSpaceDecoration(10, requireContext()))
        }
    }


    private fun setApplyIconRecyclerView() {

        binding.rvApplyIconList.apply {
            proposedIconAdapter = ChatRoomIconListAdapter(
                onQuestionClick = onQuestionRoomClick,
            )
            adapter = proposedIconAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(getVerticalSpaceDecoration(10, requireContext()))
        }

    }


    /**
     * 학생이 일반질문에서 문제를 클릭하면 보게되는 이중탭을 보이게 한다.
     */
    fun setOfferingTeacherMode() {
        binding.containerTutoringList.visibility = View.GONE
        binding.containerIconSideSection.visibility = View.VISIBLE
        binding.containerOfferingTeacher.visibility = View.VISIBLE
        binding.containerChatRoom.updateLayoutParams<ConstraintLayout.LayoutParams> {
            leftToRight = binding.containerOfferingTeacher.id
        }

    }

    /**
     * 학생이 일반질문에서 문제를 클릭하면 보게되는 이중탭을 숨긴다.
     */
    fun unSetOfferingTeacherMode() {
        binding.containerTutoringList.visibility = View.VISIBLE
        binding.containerIconSideSection.visibility = View.GONE
        binding.containerOfferingTeacher.visibility = View.GONE
        binding.containerChatRoom.updateLayoutParams<ConstraintLayout.LayoutParams> {
            leftToRight = binding.containerTutoringList.id
        }
        //resetMsgTab()
    }

    /**
     * 강의실로 이동
     */
    protected fun moveToClassRoom(classroomInfoVO: ClassroomInfoVO) {
        val intent = Intent(requireContext(), ClassroomActivity::class.java)
        classroomInfoVO.let {
            val whiteBoardRoomInfo = SerializedWhiteBoardRoomInfo(
                appId = it.boardAppId,
                uuid = it.boardUUID,
                roomToken = it.boardToken,
                uid = if (isTeacher()) "${ClassroomFragment.RTC_TEACHER_UID}" else "${ClassroomFragment.RTC_STUDENT_UID}",
                questionId = currentChatRoom?.questionId ?: "",
                roomProfileImage = currentChatRoom?.roomImage ?: "",
                opponentName = currentChatRoom?.title ?: "",
                isTeacher = isTeacher(),
                tutoringId = tutoringId
            )
            val voiceRoomInfo = SerializedVoiceRoomInfo(
                appId = it.rtcAppId,
                token = it.rtcToken,
                channelId = it.rtcChannel,
                uid = it.rtcUID,
            )
            // 교실 액티비티로 이동한다
            intent.apply {
                putExtra("whiteBoardInfo", whiteBoardRoomInfo)
                putExtra("voiceRoomInfo", voiceRoomInfo)
            }
            startActivityForResult(intent, CLASSROOM_END_RESULT)
        }
    }

    private fun chatRoomVOtoMap(rooms: List<ChatRoomVO>): Map<String, ChatRoomVO> {
        val roomInfo: MutableMap<String, ChatRoomVO> = mutableMapOf()
        rooms.forEach {
            roomInfo[it.id] = it
        }
        return roomInfo
    }


    private fun setOfferingTeacherListItems(teacher: List<ChatRoomVO>) {
        if (teacher.size == 0) {
            binding.cvOfferingEmpty.visibility = View.VISIBLE
        } else {
            binding.cvOfferingEmpty.visibility = View.GONE
        }
        offeringTeacherAdapter.setSelectedChattingRoomId(null)
        offeringTeacherAdapter.setItem(teacher.map { it.id })
        binding.tvOfferingTeacherCount.text = "${teacher.size}"
        offeringTeacherAdapter.setRoomInfo(chatRoomVOtoMap(teacher))
        offeringTeacherAdapter.notifyDataSetChanged()
    }


    private fun setProposedIconItems(items: List<ChatRoomVO>) {
        proposedIconAdapter.setItem(items)
        proposedIconAdapter.notifyDataSetChanged()
    }

    private val onQuestionRoomClick: (List<ChatRoomVO>, String, RecyclerView.Adapter<*>) -> Unit =
        { teacherList, questionId, caller ->
            setOfferingTeacherListItems(teacherList)
            setOfferingTeacherMode()
            setSelectedRoomId(null)
            proposedIconAdapter.changeSelectedQuestionId(questionId)
            setNotiVisible(false)
            setChatRoomBtnsVisible(false)
        }
    private val onTeacherRoomClick: (ChatRoomVO, RecyclerView.Adapter<*>) -> Unit =
        { chatRoom, caller ->
            enterChatRoom(chatRoom)
        }

    fun enterChatRoom(chatRoomVO: ChatRoomVO) {
        loadingDialog.dismiss() // 로딩중에 방이 바뀌는 경우에 대비해서 로딩중인 다이얼로그를 닫는다.
        currentChatRoom = chatRoomVO
        onChatRoomStateChange(chatRoomVO)
        chatViewModel.getMessages(chatRoomVO.id)
        binding.tvChatRoomTitle.text = chatRoomVO.title ?: "undefine"
        chatViewModel.markAsRead(chatRoomVO.id)
        setSelectedRoomId(chatRoomVO.id)
        Log.d("chat", "currentChatRoom: $currentChatRoom")
        scrollMessageToBottom()
        setMessageInputBoxVisibility()
        setNoNewMessageRoom(chatRoomVO.id)
        refreshTabBadge()
        socketManager.setChatRoomId(chatRoomVO.id)
    }

    private fun setNoNewMessageRoom(roomId: String) {
        recyclerViewAdapters.forEach {
            when (it) {
                is ChatRoomListAdapter -> {
                    it.noNewMessageRoom.add(roomId)
                }
            }
        }
    }

    private fun setMessageInputBoxVisibility() {
        binding.containerChatRoom.visibility =
            if (currentChatRoom == null) View.INVISIBLE else View.VISIBLE
    }

    protected fun setChatRoomRightBtnVisible(b: Boolean) {
        binding.btnChatRoomRight.visibility = if (b) View.VISIBLE else View.INVISIBLE
    }

    protected fun setChatRoomBtnsVisible(b: Boolean) {
        listOf(
            binding.btnChatRoomRight,
            binding.btnChatRoomLeft
        ).forEach {
            it.visibility = if (b) View.VISIBLE else View.GONE
        }
    }

    protected fun enableExitRoomBtn() {
        binding.btnChatRoomRight.apply {
            visibility = View.VISIBLE
            text = "채팅방 나가기"
            setBackgroundResource(R.drawable.bg_radius_100_grad_blue)
            isEnabled = true
            setTextColor(resources.getColor(R.color.white, null))
            setOnClickListener {
                SimpleConfirmDialog {
                    currentChatRoom?.id?.let { chatViewModel.exitChatRoom(it) }
                }.apply {
                    title = "현재 채팅방에서 나갈까요?"
                    if (isTeacher())
                        description = "모든 메시지가 삭제됩니다."
                    else
                        description = "채팅방에서 나가도 과외 영상은 홈에서 다시 볼 수 있어요."
                }.show(
                    parentFragmentManager,
                    "detailAlertDialog"
                )
            }
        }
    }


    protected fun setNotiVisible(b: Boolean) {
        with(binding) {
            if (b) {
                cnNoti.visibility = View.VISIBLE
                //noti 밑에서 부터 메시지 보이게
                ConstraintSet().apply {
                    clone(containerMessages)
                    clear(rvMsgs.id, ConstraintSet.TOP)
                    connect(
                        rvMsgs.id,
                        ConstraintSet.TOP,
                        cnNoti.id,
                        ConstraintSet.BOTTOM,
                    )
                    applyTo(containerMessages)
                }
            } else {
                cnNoti.visibility = View.GONE
                //noti 없으면 메시지가 맨 위에 붙게
                ConstraintSet().apply {
                    clone(containerMessages)
                    connect(
                        rvMsgs.id,
                        ConstraintSet.TOP,
                        containerMessages.id,
                        ConstraintSet.TOP,
                    )
                    applyTo(containerMessages)
                }
            }
        }
    }

    protected fun enableChatting(b: Boolean) {
        binding.etMessage.apply {
            isFocusable = b
            isFocusableInTouchMode = b
        }
    }


    private fun observeExitRoomResult() {
        chatViewModel.deleteChatRoom.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Success -> {
                    clearChatRoomState()
                    getRoomList()
                }

                else -> {}
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CLASSROOM_END_RESULT -> {
                    if (isTeacher()) return
                    val opponentName = data?.getStringExtra("opponentName")
                    val tutoringId = data?.getStringExtra("tutoringId")
                    val teacherImg = data?.getStringExtra("teacherImg")
                    if (tutoringId != null) {
                        ReviewDialog(opponentName, teacherImg) { rating, comment ->
                            reviewViewModel.createReview(
                                tutoringId = tutoringId,
                                rating = rating,
                                comment = comment
                            )

                        }.show(parentFragmentManager, "reviewDialog")
                    }
                }
            }
        }
    }

    companion object {
        private const val PROPOSED_QUESTION_CONTAINER_MAX_HEIGHT = 200
    }
}
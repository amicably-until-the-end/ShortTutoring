package org.softwaremaestro.presenter.chat_page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
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
import org.softwaremaestro.presenter.classroom.item.SerializedVoiceRoomInfo
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.databinding.FragmentChatPageBinding
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

    protected var currentChatRoom: ChatRoomVO? = null

    lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var socketManager: SocketManager

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
        getRoomList()
        setSendMessageButton()
        observeMessages()
        observeSocket()
        observeCurrentRoom()

        clearChatRoomState()


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireContext())
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

    private fun observeCurrentRoom() {
        chatViewModel.currentChattingRoomVO.observe(viewLifecycleOwner) {
            it?.let {
                enterChatRoom(it)
            }
        }
    }

    private fun clearChatRoomState() {
        Log.d("chat", "clearChatRoomState")
        currentChatRoom = null
        binding.tvChatRoomTitle.text = ""
        binding.btnChatRoomRight.visibility = View.GONE
        messageListAdapter.setItem(emptyList())
    }

    private fun observeSocket() {
        //viewmodel로 뺄 수 도 있겠다.
        socketManager.addOnMessageReceiveListener { chatRoomId ->
            if (currentChatRoom?.id == chatRoomId) {
                chatViewModel.getMessages(chatRoomId)
            }
            chatViewModel.getChatRoomList(isTeacher(), currentChatRoom?.id)
        }
    }

    private fun sendMessage() {
        if (binding.etMessage.text.isNullOrEmpty()) return
        currentChatRoom?.let {
            chatViewModel.sendMessage(
                binding.etMessage.text.toString(),
                chattingId = it.id!!,
                receiverId = it.opponentId!!,
            )
        }
    }

    private fun refreshProposedRoomList() {
        if (binding.rbNormalQuestion.isChecked) {
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
        if (binding.rbNormalQuestion.isChecked) {
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


    private fun observeChatRoomList() {
        chatViewModel.proposedSelectedChatRoomList.observe(viewLifecycleOwner) {
            refreshProposedRoomList()

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
        }
        chatViewModel.reservedSelectedChatRoomList.observe(viewLifecycleOwner) {
            refreshReservedRoomList()
        }
        chatViewModel.reservedNormalChatRoomList.observe(viewLifecycleOwner) {
            refreshReservedRoomList()
        }
    }


    /**
     * 모든 어댑터를 초기화 한 이후에 실행해야 함
     */
    private fun makeAdapterList() {
        recyclerViewAdapters.apply {
            add(proposedAdapter)
            add(reservedAdapter)
            add(messageListAdapter)
            add(offeringTeacherAdapter)
            add(proposedIconAdapter)
        }
    }

    private fun setCloseOfferingTeacherButton() {
        binding.btnCloseOfferingTeacher.setOnClickListener {
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

                is ChatRoomIconListAdapter -> {
                    it.changeSelectedQuestionId(selectedRoomId)
                }
            }
        }

    }

    private fun setQuestionTypeSelectToggle() {
        binding.rgTutoringList.setOnCheckedChangeListener { _, checkId ->
            setSelectedRoomId(null)

            when (checkId) {
                R.id.rb_normal_question -> {
                    setReservedSectionItems(
                        chatViewModel.reservedNormalChatRoomList.value?._data ?: emptyList()
                    )
                    setProposedSectionItems(
                        chatViewModel.proposedNormalChatRoomList.value?._data ?: emptyList()
                    )
                }

                R.id.rb_selected_question -> {
                    setReservedSectionItems(
                        chatViewModel.reservedSelectedChatRoomList.value?._data ?: emptyList()
                    )
                    setProposedSectionItems(
                        chatViewModel.proposedSelectedChatRoomList.value?._data ?: emptyList()
                    )
                }
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
            reservedAdapter.setItem(list)
            reservedAdapter.notifyDataSetChanged()
        }
    }

    private fun setProposedSectionItems(list: List<ChatRoomVO>) {
        binding.apply {
            tvApplyCount.text = list.size.toString()
            cvQuestionProposedEmpty.visibility = if (list.isNotEmpty()) View.GONE else View.VISIBLE
            proposedAdapter.setItem(list)
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

    private fun setMessageListItems(messages: List<MessageVO>) {
        messageListAdapter.setItem(
            messages
        )
        messageListAdapter.notifyDataSetChanged()
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


    fun setOfferingTeacherMode() {
        binding.containerTutoringList.visibility = View.GONE
        binding.containerIconSideSection.visibility = View.VISIBLE
        binding.containerOfferingTeacher.visibility = View.VISIBLE
        binding.containerChatRoom.updateLayoutParams<ConstraintLayout.LayoutParams> {
            leftToRight = binding.containerOfferingTeacher.id
        }

    }

    fun unSetOfferingTeacherMode() {
        binding.containerTutoringList.visibility = View.VISIBLE
        binding.containerIconSideSection.visibility = View.GONE
        binding.containerOfferingTeacher.visibility = View.GONE
        binding.containerChatRoom.updateLayoutParams<ConstraintLayout.LayoutParams> {
            leftToRight = binding.containerTutoringList.id
        }
        //resetMsgTab()
    }

    protected fun moveToClassRoom(classroomInfoVO: ClassroomInfoVO) {
        val intent = Intent(requireContext(), ClassroomActivity::class.java)
        classroomInfoVO.let {
            val whiteBoardRoomInfo = SerializedWhiteBoardRoomInfo(
                appId = it.boardAppId,
                uuid = it.boardUUID,
                roomToken = it.boardToken,
                uid = if (isTeacher()) "${ClassroomFragment.RTC_STUDENT_UID}" else "${ClassroomFragment.RTC_TEACHER_UID}"
            )
            val voiceRoomInfo = SerializedVoiceRoomInfo(
                appId = it.rtcAppId,
                token = it.rtcToken,
                channelId = it.rtcChannel,
                uid = if (isTeacher()) (ClassroomFragment.RTC_TEACHER_UID) else (ClassroomFragment.RTC_STUDENT_UID)
            )
            // 교실 액티비티로 이동한다
            intent.apply {
                putExtra("whiteBoardInfo", whiteBoardRoomInfo)
                putExtra("voiceRoomInfo", voiceRoomInfo)
            }
            startActivity(intent)
        }
    }


    private fun setOfferingTeacherListItems(teacher: List<ChatRoomVO>) {
        Log.d("chat", teacher.toString())
        offeringTeacherAdapter.setSelectedChattingRoomId(null)
        offeringTeacherAdapter.setItem(teacher)
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
        chatViewModel.getMessages(chatRoomVO.id!!)
        binding.tvChatRoomTitle.text = chatRoomVO.title ?: "undefine"
        setSelectedRoomId(chatRoomVO.id)
        Log.d("chat", "currentChatRoom: $currentChatRoom")
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

    protected fun setNotiVisible(b: Boolean) {
        binding.cnNoti.visibility = if (b) View.VISIBLE else View.GONE
    }

}
package org.softwaremaestro.presenter.chat_page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.util.getVerticalSpaceDecoration
import org.softwaremaestro.presenter.chat_page.adapter.ChatRoomIconListAdapter
import org.softwaremaestro.presenter.chat_page.adapter.MessageListAdapter
import org.softwaremaestro.presenter.chat_page.adapter.ChatRoomListAdapter
import org.softwaremaestro.presenter.chat_page.viewmodel.ChatViewModel
import org.softwaremaestro.presenter.classroom.ClassroomActivity
import org.softwaremaestro.presenter.classroom.ClassroomFragment
import org.softwaremaestro.presenter.classroom.item.SerializedVoiceRoomInfo
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.databinding.FragmentChatPageBinding
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.widget.LoadingDialog


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

    private val chatViewModel: ChatViewModel by activityViewModels();

    protected var currentChatRoom: ChatRoomVO? = null

    lateinit var loadingDialog: LoadingDialog

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


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireContext())

    }


    private fun setSendMessageButton() {
        binding.btnSendMessage.setOnClickListener {
            chatViewModel.sendMessage(binding.etMessage.text.toString())
        }
    }

    private fun getRoomList() {
        chatViewModel.getChatRoomList()
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

    fun observeTutoringInfo() {
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

            }
        }
    }

    fun enterRoom() {
        if (currentChatRoom?.questionId == null) {
            Toast.makeText(requireContext(), "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        chatViewModel.getClassRoomInfo(currentChatRoom?.questionId!!)
        observeTutoringInfo()
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


    private fun clearRecyclersSelectedView(caller: RecyclerView.Adapter<*>?) {

        recyclerViewAdapters.listIterator().forEach {
            when (it) {
                is ChatRoomListAdapter -> {
                    it.clearSelectedItem(caller)
                }

                is ChatRoomIconListAdapter -> {
                    it.clearSelectedView(caller)
                }
            }
        }

    }

    private fun setQuestionTypeSelectToggle() {
        binding.rgTutoringList.setOnCheckedChangeListener { _, checkId ->
            clearRecyclersSelectedView(null)

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

    abstract fun onChatRoomStateChange(chatRoomVO: ChatRoomVO)

    private fun setReservedSectionItems(list: List<ChatRoomVO>) {
        binding.apply {
            tvReservedCount.text = list.size.toString()
            reservedAdapter.setItem(list)
            reservedAdapter.notifyDataSetChanged()
        }
    }

    private fun setProposedSectionItems(list: List<ChatRoomVO>) {
        binding.apply {
            tvApplyCount.text = list.size.toString()
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
        messageListAdapter = MessageListAdapter()
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


    private fun setOfferingTeacherMode() {
        binding.containerTutoringList.visibility = View.GONE
        binding.containerIconSideSection.visibility = View.VISIBLE
        binding.containerOfferingTeacher.visibility = View.VISIBLE
        binding.containerChatRoom.updateLayoutParams<ConstraintLayout.LayoutParams> {
            leftToRight = binding.containerOfferingTeacher.id
        }

    }

    private fun unSetOfferingTeacherMode() {
        binding.containerTutoringList.visibility = View.VISIBLE
        binding.containerIconSideSection.visibility = View.GONE
        binding.containerOfferingTeacher.visibility = View.GONE
        binding.containerChatRoom.updateLayoutParams<ConstraintLayout.LayoutParams> {
            leftToRight = binding.containerTutoringList.id
        }
    }

    private fun moveToClassRoom(tutoringInfoVO: TutoringInfoVO) {
        val intent = Intent(requireContext(), ClassroomActivity::class.java)
        tutoringInfoVO.let {
            val whiteBoardRoomInfo = SerializedWhiteBoardRoomInfo(
                it.whiteBoardAppId!!,
                it.whiteBoardUUID!!,
                it.whiteBoardToken!!,
                "1"
            )
            val voiceRoomInfo = SerializedVoiceRoomInfo(
                appId = it.RTCAppId!!,
                token = if (isTeacher()) (it.teacherRTCToken) else (it.studentRTCToken),
                channelId = it.id,
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
        offeringTeacherAdapter.clearSelectedItem(null)
        offeringTeacherAdapter.setItem(teacher)
        offeringTeacherAdapter.notifyDataSetChanged()
    }


    private fun setProposedIconItems(items: List<ChatRoomVO>) {
        proposedIconAdapter.setItem(items)
        proposedIconAdapter.notifyDataSetChanged()
    }


    private val onQuestionRoomClick: (List<ChatRoomVO>, Int, RecyclerView.Adapter<*>) -> Unit =
        { teacherList, position, caller ->
            setOfferingTeacherListItems(teacherList)
            setOfferingTeacherMode()
            clearRecyclersSelectedView(null)
            proposedIconAdapter.setSelectedPosition(position)
        }
    private val onTeacherRoomClick: (ChatRoomVO, RecyclerView.Adapter<*>) -> Unit =
        { chatRoom, caller ->
            currentChatRoom = chatRoom
            setMessageListItems(chatRoom.messages ?: emptyList())
            onChatRoomStateChange(chatRoom)
            binding.tvChatRoomTitle.text = chatRoom.title
            clearRecyclersSelectedView(caller)
        }


}
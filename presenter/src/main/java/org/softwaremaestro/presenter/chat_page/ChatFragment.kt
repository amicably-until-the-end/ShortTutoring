package org.softwaremaestro.presenter.chat_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.MessageVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.util.getVerticalSpaceDecoration
import org.softwaremaestro.presenter.chat_page.adapter.ChatRoomIconListAdapter
import org.softwaremaestro.presenter.chat_page.adapter.MessageListAdapter
import org.softwaremaestro.presenter.chat_page.adapter.ChatRoomListAdapter
import org.softwaremaestro.presenter.chat_page.viewmodel.ChatViewModel
import org.softwaremaestro.presenter.databinding.FragmentChatPageBinding
import org.softwaremaestro.presenter.util.UIState


@AndroidEntryPoint
abstract class ChatFragment : Fragment() {

    protected lateinit var binding: FragmentChatPageBinding
    private lateinit var proposedAdapter: ChatRoomListAdapter
    private lateinit var reservedAdapter: ChatRoomListAdapter
    private lateinit var messageListAdapter: MessageListAdapter
    private lateinit var offeringTeacherAdapter: ChatRoomListAdapter
    private lateinit var applyIconAdapter: ChatRoomIconListAdapter
    private lateinit var reservedIconAdapter: ChatRoomIconListAdapter

    private var selectedTutoringIndex: Int? = null
    private var selectedTeacher: Int? = null

    private var recyclerViewAdapters: MutableList<RecyclerView.Adapter<*>> = mutableListOf()

    private val chatViewModel: ChatViewModel by activityViewModels();


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
        setReservedIconRecyclerView()
        setOfferingTeacherRecyclerView()
        setCloseOfferingTeacherButton()
        setChatRoomRightButton()
        observeChatRoomList()
        makeAdapterList()
        getRoomList()
        setSendMessageButton()

        return binding.root

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

    private fun observeChatRoomList() {
        chatViewModel.proposedSelectedChatRoomList.observe(viewLifecycleOwner) {
            refreshProposedRoomList()
        }
        chatViewModel.proposedNormalChatRoomList.observe(viewLifecycleOwner) {
            refreshProposedRoomList()
        }
        chatViewModel.reservedSelectedChatRoomList.observe(viewLifecycleOwner) {
            refreshReservedRoomList()
        }
        chatViewModel.reservedNormalChatRoomList.observe(viewLifecycleOwner) {
            refreshReservedRoomList()
        }
    }

    abstract fun onChatRightOptionButtonClick(): Unit
    abstract fun onChatLeftOptionButtonClick(): Unit


    private fun setChatRoomRightButton() {
        binding.btnChatRoomRight.setOnClickListener {
            onChatRightOptionButtonClick()
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
            add(applyIconAdapter)
            add(reservedIconAdapter)
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
            applyIconAdapter = ChatRoomIconListAdapter(
                onQuestionClick = onQuestionRoomClick,
            )
            adapter = applyIconAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(getVerticalSpaceDecoration(10, requireContext()))
        }

    }

    private fun setReservedIconRecyclerView() {

        binding.rvReservedIconList.apply {
            reservedIconAdapter = ChatRoomIconListAdapter(
                onQuestionClick = onQuestionRoomClick,
            )
            adapter = reservedIconAdapter
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


    private fun setOfferingTeacherListItems(teacher: List<ChatRoomVO>) {
        offeringTeacherAdapter.setItem(teacher)
        offeringTeacherAdapter.notifyDataSetChanged()
    }

    private fun setReservedIconItems(items: List<ChatRoomVO>) {
        offeringTeacherAdapter.setItem(items)
        offeringTeacherAdapter.notifyDataSetChanged()
    }

    private fun setProposedIconItems(items: List<ChatRoomVO>) {
        applyIconAdapter.setItem(items)
        applyIconAdapter.notifyDataSetChanged()
    }


    private val onQuestionRoomClick: (String, Int, RecyclerView.Adapter<*>) -> Unit =
        { questionId, position, caller ->
            setOfferingTeacherMode()
            clearRecyclersSelectedView(null)
            applyIconAdapter.setSelectedPosition(position)
        }
    private val onTeacherRoomClick: (String, List<MessageVO>, RecyclerView.Adapter<*>) -> Unit =
        { teacherId, messages, caller ->
            setMessageListItems(messages)

            clearRecyclersSelectedView(caller)
        }


}
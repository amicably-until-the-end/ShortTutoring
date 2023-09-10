package org.softwaremaestro.presenter.chat_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.Util.DetailAlertDialog
import org.softwaremaestro.presenter.Util.getVerticalSpaceDecoration
import org.softwaremaestro.presenter.chat_page.item.ChatMsg
import org.softwaremaestro.presenter.chat_page.item.ChatRoom
import org.softwaremaestro.presenter.chat_page.adapter.ChatRoomIconListAdapter
import org.softwaremaestro.presenter.chat_page.adapter.MessageListAdapter
import org.softwaremaestro.presenter.chat_page.adapter.ChatRoomListAdapter
import org.softwaremaestro.presenter.databinding.FragmentChatPageBinding


abstract class ChatFragment : Fragment() {

    protected lateinit var binding: FragmentChatPageBinding
    private lateinit var applyAdapter: ChatRoomListAdapter
    private lateinit var reservedAdapter: ChatRoomListAdapter
    private lateinit var messageListAdapter: MessageListAdapter
    private lateinit var offeringTeacherAdapter: ChatRoomListAdapter
    private lateinit var applyIconAdapter: ChatRoomIconListAdapter
    private lateinit var reservedIconAdapter: ChatRoomIconListAdapter

    private var selectedTutoringIndex: Int? = null
    private var selectedTeacher: Int? = null

    private var recyclerViewAdapters: MutableList<RecyclerView.Adapter<*>> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatPageBinding.inflate(inflater, container, false)

        setApplyRecyclerView()
        setReservedRecyclerView()
        setChatMsgRecyclerView()
        setQuestionTypeSelectToggle()
        setApplyIconRecyclerView()
        setReservedIconRecyclerView()
        setOfferingTeacherRecyclerView()
        setCloseOfferingTeacherButton()
        setChatRoomRightButton()
        makeAdapterList()

        return binding.root

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
            add(applyAdapter)
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

    private fun setApplyRecyclerView() {

        applyAdapter = ChatRoomListAdapter(
            onQuestionClick = onQuestionRoomClick,
            onTeacherClick = onTeacherRoomClick,
        )
        binding.rvApplyList.apply {
            adapter = applyAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(getVerticalSpaceDecoration(10, requireContext()))
        }
        applyAdapter.setItem(
            listOf(
                ChatRoom(
                    title = "강해린 쌤",
                    roomType = 1,
                    subTitle = "영어",
                    id = "1",
                    imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                    contentId = "d",
                    newMessage = 0
                ),
                ChatRoom(
                    title = "팜하니 쌤",
                    roomType = 0,
                    subTitle = "수학",
                    id = "2",
                    imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                    contentId = "d",
                    newMessage = 0
                )
            )
        )
        applyAdapter.notifyDataSetChanged()
        binding.tvApplyCount.text = applyAdapter.itemCount.toString()
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
                    setApplySectionItems(applyList)
                    setReservedSectionItems(reservList)
                }

                R.id.rb_selected_question -> {
                    //TODO : GET SELECTED QUESTION LIST FROM SERVER
                    setApplySectionItems(applyList)
                    setReservedSectionItems(reservList)
                }
            }
        }
    }

    private fun setReservedSectionItems(list: List<ChatRoom>) {
        binding.apply {
            tvReservedCount.text = list.size.toString()
            reservedAdapter.setItem(list)
            reservedAdapter.notifyDataSetChanged()
        }
    }

    private fun setApplySectionItems(list: List<ChatRoom>) {
        binding.apply {
            tvApplyCount.text = list.size.toString()
            applyAdapter.setItem(list)
            applyAdapter.notifyDataSetChanged()
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
        messageListAdapter.setItem(
            listOf(
                ChatMsg(
                    id = "1",
                    myMsg = true,
                    message = "첫번째 내가 보낸 텍스트 메시지",
                    time = "오후 5:00",
                    buttonNumber = 0,
                    buttonString = null,
                    type = MessageListAdapter.TYPE_TEXT,
                    questionDesc = null,
                    questionImageUrl = null,
                    questionId = null
                ),
                ChatMsg(
                    id = "2",
                    myMsg = false,
                    message = "첫번째 상대방이 보낸 텍스트 메시지",
                    time = "오후 5:01",
                    buttonNumber = 0,
                    buttonString = null,
                    type = MessageListAdapter.TYPE_TEXT,
                    questionDesc = null,
                    questionImageUrl = null,
                    questionId = null
                ),
                ChatMsg(
                    id = "3",
                    myMsg = false,
                    message = "두번째 내가 보낸 질문 메시지",
                    time = "오후 5:02",
                    buttonNumber = 0,
                    buttonString = null,
                    type = MessageListAdapter.TYPE_QUESTION,
                    questionDesc = "아무리 봐도 모르는 문제입니다. 도와주세요",
                    questionImageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                    questionId = "1234"
                ),
                ChatMsg(
                    id = "4",
                    myMsg = false,
                    message = "두번째 상대방이 보낸 버튼 메시지",
                    time = "오후 5:03",
                    buttonNumber = 3,
                    buttonString = listOf("예", "아니오", "모르겠어요"),
                    type = MessageListAdapter.TYPE_BUTTONS,
                    questionDesc = null,
                    questionImageUrl = null,
                    questionId = null
                ),
            )
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
        offeringTeacherAdapter.setItem(offerList)
        offeringTeacherAdapter.notifyDataSetChanged()
    }

    private fun setApplyIconRecyclerView() {

        binding.rvApplyIconList.apply {
            applyIconAdapter = ChatRoomIconListAdapter(
                onQuestionClick = onQuestionRoomClick,
                onTeacherClick = onTeacherRoomClick,
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
                onTeacherClick = onTeacherRoomClick,
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


    private fun setOfferingTeacherListItems(teacher: List<ChatRoom>) {
        offeringTeacherAdapter.setItem(teacher)
        offeringTeacherAdapter.notifyDataSetChanged()
    }

    private fun setReservedIconItems(items: List<ChatRoom>) {
        offeringTeacherAdapter.setItem(items)
        offeringTeacherAdapter.notifyDataSetChanged()
    }

    private fun setApplyIconItems(items: List<ChatRoom>) {
        applyIconAdapter.setItem(items)
        applyIconAdapter.notifyDataSetChanged()
    }


    private val onQuestionRoomClick: (String, Int, RecyclerView.Adapter<*>) -> Unit =
        { questionId, position, caller ->
            setOfferingTeacherMode()
            setOfferingTeacherListItems(offerList)
            setApplyIconItems(applyList)
            setReservedIconItems(reservList)
            clearRecyclersSelectedView(null)
            applyIconAdapter.setSelectedPosition(position)
        }
    private val onTeacherRoomClick: (String, RecyclerView.Adapter<*>) -> Unit =
        { teacherId, caller ->
            clearRecyclersSelectedView(caller)
        }

    companion object {
        val offerList = listOf(
            ChatRoom(
                title = "강해린 쌤",
                roomType = 0,
                subTitle = "오늘 13:00",
                id = "1",
                imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                contentId = "d",
                newMessage = 0
            ),
            ChatRoom(
                title = "팜하니 쌤",
                roomType = 0,
                subTitle = "오늘 15:00",
                id = "2",
                imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                contentId = "d",
                newMessage = 0
            )
        )
        val applyList = listOf(
            ChatRoom(
                title = "강해린 쌤",
                roomType = 1,
                subTitle = "영어",
                id = "1",
                imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                contentId = "d",
                newMessage = 2
            ),
            ChatRoom(
                title = "팜하니 쌤",
                roomType = 0,
                subTitle = "수학",
                id = "2",
                imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                contentId = "d",
                newMessage = 0
            )
        )
        val reservList = listOf(
            ChatRoom(
                title = "강해린 쌤",
                roomType = 0,
                subTitle = "오늘 13:00",
                id = "1",
                imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                contentId = "d",
                newMessage = 3
            ),
            ChatRoom(
                title = "팜하니 쌤",
                roomType = 0,
                subTitle = "오늘 15:00",
                id = "2",
                imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                contentId = "d",
                newMessage = 1
            )
        )
    }


}
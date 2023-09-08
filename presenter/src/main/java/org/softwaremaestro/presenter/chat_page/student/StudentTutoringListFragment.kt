package org.softwaremaestro.presenter.chat_page.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.Util.getVerticalSpaceDecoration
import org.softwaremaestro.presenter.chat_page.item.ChatMsg
import org.softwaremaestro.presenter.chat_page.item.ChatRoom
import org.softwaremaestro.presenter.chat_page.student.adapter.MessageListAdapter
import org.softwaremaestro.presenter.chat_page.student.adapter.ChatListTeacherAdapter
import org.softwaremaestro.presenter.databinding.FragmentStudentTutoringListBinding


class StudentTutoringListFragment : Fragment() {

    private lateinit var binding: FragmentStudentTutoringListBinding
    private lateinit var applyAdapter: ChatListTeacherAdapter
    private lateinit var reservedAdapter: ChatListTeacherAdapter
    private lateinit var messageListAdapter: MessageListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStudentTutoringListBinding.inflate(inflater, container, false)

        setApplyRecyclerView()
        setReservedRecyclerView()
        setChatMsgRecyclerView()
        setQuestionTypeSelectToggle()

        return binding.root

    }

    private fun setApplyRecyclerView() {


        applyAdapter = ChatListTeacherAdapter()
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

    private fun setQuestionTypeSelectToggle() {
        binding.rgTutoringList.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.rb_normal_question -> {
                    val applyList = listOf(
                        ChatRoom(
                            title = "문제 이해가 안돼요 ㅠㅠ",
                            roomType = 1,
                            subTitle = " 고등학교 · 미적분",
                            id = "1",
                            imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                            contentId = "d",
                            newMessage = 0
                        ),
                        ChatRoom(
                            title = "미분 하는 방법을 알려주세요",
                            roomType = 1,
                            subTitle = "고등학교 · 수학(상)",
                            id = "2",
                            imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                            contentId = "d",
                            newMessage = 0
                        )
                    )
                    setApplySectionItems(applyList)
                    val reservList = listOf(
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
                    setReservedSectionItems(reservList)
                }

                R.id.rb_selected_question -> {
                    //TODO : GET SELECTED QUESTION LIST FROM SERVER
                    val applyList = listOf(
                        ChatRoom(
                            title = "다니엘 쌤",
                            roomType = 0,
                            subTitle = "영어",
                            id = "1",
                            imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                            contentId = "d",
                            newMessage = 0
                        ),
                        ChatRoom(
                            title = "민지 쌤",
                            roomType = 0,
                            subTitle = "수학",
                            id = "2",
                            imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                            contentId = "d",
                            newMessage = 0
                        )
                    )
                    setApplySectionItems(applyList)
                    val reservList = listOf(
                        ChatRoom(
                            title = "팜하니 쌤",
                            roomType = 0,
                            subTitle = "8월 5일 14:00",
                            id = "1",
                            imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                            contentId = "d",
                            newMessage = 0
                        ),
                        ChatRoom(
                            title = "혜인 쌤",
                            roomType = 0,
                            subTitle = "8월 6일 15:00",
                            id = "2",
                            imageUrl = "https://chat.openai.com/_next/image?url=https%3A%2F%2Flh3.googleusercontent.com%2Fa%2FAAcHTtcaPO8nwFeU42FJpHZ6N7jkBX4_T6ziRAhKpwDC7eM4iQ%3Ds96-c&w=96&q=75",
                            contentId = "d",
                            newMessage = 0
                        )
                    )
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
        reservedAdapter = ChatListTeacherAdapter()
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


}
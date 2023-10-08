package org.softwaremaestro.presenter.chat_page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.domain.chat.entity.RoomType
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemTutoringListRoomBinding
import org.softwaremaestro.presenter.util.Util

class ChatRoomListAdapter(
    private val onQuestionClick: (List<ChatRoomVO>, String, RecyclerView.Adapter<*>) -> Unit,
    private val onTeacherClick: (ChatRoomVO, RecyclerView.Adapter<*>) -> Unit
) :
    RecyclerView.Adapter<ChatRoomListAdapter.ViewHolder>() {

    private var chatRoomIdList: List<String> = emptyList()

    private var roomInfo: Map<String, ChatRoomVO>? = null

    private var selectedView: MaterialCardView? = null

    private var selectedChattingRoomId: String? = null

    val noNewMessageRoom = mutableSetOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ItemTutoringListRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(roomInfo?.get(chatRoomIdList[position])!!, position)
    }

    override fun getItemCount(): Int {
        return chatRoomIdList.size
    }

    fun setItem(items: List<String>) {
        this.chatRoomIdList = items
        noNewMessageRoom.clear()
    }

    fun setRoomInfo(roomInfo: Map<String, ChatRoomVO>) {
        this.roomInfo = roomInfo
        noNewMessageRoom.forEach {
            roomInfo[it]?.messages = 0
        }
        notifyDataSetChanged()
    }

    fun setSelectedChattingRoomId(chattingId: String?) {
        selectedChattingRoomId = chattingId
        notifyDataSetChanged()
    }


    fun decorateSelectedView(selectedView: View?) {
        selectedView?.setBackgroundResource(R.drawable.bg_shadow_1_stroke_primary_blue)
    }

    fun unDecorateSelectedView(selectedView: View?) {
        selectedView?.setBackgroundResource(R.drawable.bg_shadow_1_stroke_background_grey)
    }

    inner class ViewHolder(private val binding: ItemTutoringListRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatRoomVO, position: Int) {
            binding.apply {
                when (item.roomType) {
                    RoomType.QUESTION -> {
                        root.setOnClickListener {
                            onQuestionClick(
                                item.teachers ?: emptyList(), item.questionId,
                                this@ChatRoomListAdapter
                            )
                        }
                        cvImage.radius = Util.toPx(4, binding.root.context).toFloat()
                    }

                    RoomType.TEACHER -> {
                        if (selectedChattingRoomId == item.id) {
                            decorateSelectedView(cvContainer)
                        } else {
                            unDecorateSelectedView(cvContainer)
                        }
                        cvImage.radius = Util.toPx(20, binding.root.context).toFloat()
                        root.setOnClickListener {
                            roomInfo?.get(item.id)?.messages = 0
                            onTeacherClick(
                                item,
                                this@ChatRoomListAdapter
                            )
                        }
                    }
                }
                val messageCnt = when (item.roomType) {
                    RoomType.QUESTION -> {
                        var cnt = 0
                        item.teachers?.listIterator()?.forEach { teacherRoom ->
                            cnt += teacherRoom.messages ?: 0
                        }
                        cnt
                    }

                    RoomType.TEACHER -> {
                        item.messages ?: 0
                    }

                }
                if (messageCnt > 0 && selectedChattingRoomId != item.id && !noNewMessageRoom.contains(
                        item.id
                    )
                ) {
                    tvNewMsgCnt.visibility = View.VISIBLE
                    tvNewMsgCnt.text = messageCnt.toString()

                } else {
                    tvNewMsgCnt.visibility = View.GONE
                }


                tvTitle.text = item.title
                tvSubTitle.text = item.subTitle
                Glide.with(binding.root.context)
                    .load(item.roomImage)
                    .into(ivImage)
            }
        }
    }
}
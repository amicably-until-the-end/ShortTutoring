package org.softwaremaestro.presenter.chat_page.adapter

import android.view.LayoutInflater
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

    var roomInfo: Map<String, ChatRoomVO>? = null

    private var selectedView: MaterialCardView? = null

    private var selectedChattingRoomId: String? = null

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
    }


    fun setSelectedChattingRoomId(chattingId: String?) {
        selectedChattingRoomId = chattingId
        notifyDataSetChanged()
    }


    fun decorateSelectedView(selectedView: MaterialCardView?) {
        selectedView?.strokeColor = selectedView?.context?.getColor(R.color.primary_blue)!!
        selectedView.setBackgroundColor(selectedView.context?.getColor(R.color.background_light_blue)!!)
    }

    fun unDecorateSelectedView(selectedView: MaterialCardView?) {
        selectedView?.strokeColor = selectedView?.context?.getColor(R.color.background_grey)!!
        selectedView.setBackgroundColor(selectedView.context?.getColor(R.color.transparent)!!)
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
                if (messageCnt > 0 && selectedChattingRoomId != item.id) {
                    tvNewMsgCnt.visibility = android.view.View.VISIBLE
                    tvNewMsgCnt.text = messageCnt.toString()

                } else {
                    tvNewMsgCnt.visibility = android.view.View.GONE
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
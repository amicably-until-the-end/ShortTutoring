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

    private var items: List<ChatRoomVO> = emptyList()

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
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<ChatRoomVO>) {
        this.items = items
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
                                item.teachers ?: emptyList(), item.questionId ?: "",
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
                            onTeacherClick(
                                item,
                                this@ChatRoomListAdapter
                            )
                        }
                    }
                }
                if (!item.messages.isNullOrEmpty()) {
                    tvNewMsgCnt.visibility = android.view.View.VISIBLE
                    when (item.roomType) {
                        RoomType.QUESTION -> {
                            var cnt = 0
                            item.teachers?.listIterator()?.forEach { teacherRoom ->
                                cnt += teacherRoom.messages?.size ?: 0
                            }
                            tvNewMsgCnt.text = cnt.toString()
                        }

                        RoomType.TEACHER -> {
                            tvNewMsgCnt.text = item.messages?.size.toString()
                        }
                    }
                } else {
                    tvNewMsgCnt.visibility = android.view.View.GONE
                }

                tvTitle.text = item.title
                tvSubTitle.text = item.schoolSubject
                Glide.with(binding.root.context)
                    .load(item.roomImage)
                    .into(ivImage)
            }
        }
    }
}
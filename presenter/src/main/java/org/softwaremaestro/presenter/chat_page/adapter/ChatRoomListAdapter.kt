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
    private val onQuestionClick: (List<ChatRoomVO>, Int, RecyclerView.Adapter<*>) -> Unit,
    private val onTeacherClick: (ChatRoomVO, RecyclerView.Adapter<*>) -> Unit
) :
    RecyclerView.Adapter<ChatRoomListAdapter.ViewHolder>() {

    private var items: List<ChatRoomVO> = emptyList()

    private var selectedView: MaterialCardView? = null

    private var selectedPosition: Int = -1

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

    fun clearSelectedItem(caller: RecyclerView.Adapter<*>?) {
        if (caller !== this@ChatRoomListAdapter) {
            selectedView?.strokeColor = selectedView?.context?.getColor(R.color.background_grey)!!
            selectedView?.setBackgroundColor(selectedView?.context?.getColor(R.color.transparent)!!)
            selectedView = null
        }
    }

    inner class ViewHolder(private val binding: ItemTutoringListRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatRoomVO, position: Int) {
            binding.apply {
                when (item.roomType) {
                    RoomType.QUESTION -> {
                        root.setOnClickListener {
                            onQuestionClick(
                                item.teachers ?: emptyList(), position,
                                this@ChatRoomListAdapter
                            )
                        }
                        cvImage.radius = Util.toPx(4, binding.root.context).toFloat()
                    }

                    RoomType.TEACHER -> {
                        cvImage.radius = Util.toPx(20, binding.root.context).toFloat()
                        root.setOnClickListener {
                            onTeacherClick(
                                item,
                                this@ChatRoomListAdapter
                            )
                            clearSelectedItem(null)
                            cvContainer.strokeColor =
                                binding.root.context.getColor(R.color.primary_blue)
                            cvContainer.setBackgroundColor(binding.root.context.getColor(R.color.background_light_blue))
                            selectedView = cvContainer
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
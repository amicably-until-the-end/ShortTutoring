package org.softwaremaestro.presenter.chat_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import org.softwaremaestro.domain.chat.entity.ChatRoomVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.chat_page.item.ChatRoom
import org.softwaremaestro.presenter.databinding.ItemTutoringListRoomBinding

class ChatRoomListAdapter(
    private val onQuestionClick: (String, Int, RecyclerView.Adapter<*>) -> Unit,
    private val onTeacherClick: (String, RecyclerView.Adapter<*>) -> Unit
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
            selectedView = null
        }
    }

    inner class ViewHolder(private val binding: ItemTutoringListRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatRoomVO, position: Int) {
            binding.apply {
                if (item.roomType == 1) {
                    root.setOnClickListener {
                        onQuestionClick(
                            item.questionInfo.id, position,
                            this@ChatRoomListAdapter
                        )
                    }
                    cvImage.radius = Util.toPx(4, binding.root.context).toFloat()
                } else {
                    cvImage.radius = Util.toPx(20, binding.root.context).toFloat()
                    root.setOnClickListener {
                        onTeacherClick(item.questionInfo.id, this@ChatRoomListAdapter)
                        clearSelectedItem(null)
                        cvContainer.strokeColor =
                            binding.root.context.getColor(R.color.primary_blue)
                        selectedView = cvContainer
                    }

                }
                if (item.newMessage > 0) {
                    tvNewMsgCnt.text = item.newMessage.toString()
                    tvNewMsgCnt.visibility = android.view.View.VISIBLE
                } else {
                    tvNewMsgCnt.visibility = android.view.View.GONE
                }

                tvTitle.text = item.questionInfo.title
                tvSubTitle.text = item.questionInfo.category
                Glide.with(binding.root.context)
                    .load(item.questionInfo.imageUrl)
                    .into(ivImage)
            }
        }
    }
}
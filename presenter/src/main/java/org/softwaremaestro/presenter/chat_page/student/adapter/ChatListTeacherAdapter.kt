package org.softwaremaestro.presenter.chat_page.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.presenter.Util.Util
import org.softwaremaestro.presenter.chat_page.item.ChatRoom
import org.softwaremaestro.presenter.databinding.ItemTutoringListRoomBinding

class ChatListTeacherAdapter : RecyclerView.Adapter<ChatListTeacherAdapter.ViewHolder>() {

    private var items: List<ChatRoom> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListTeacherAdapter.ViewHolder {
        val view = ItemTutoringListRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListTeacherAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<ChatRoom>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTutoringListRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ChatRoom) {
            binding.apply {
                if (item.roomType == 1) {
                    cvImage.radius = Util.toPx(4, binding.root.context).toFloat()
                } else {
                    cvImage.radius = Util.toPx(20, binding.root.context).toFloat()
                }
                tvTitle.text = item.title
                tvSubTitle.text = item.subTitle
                Glide.with(binding.root.context)
                    .load(item.imageUrl)
                    .into(ivImage)
            }
        }
    }
}
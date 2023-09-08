package org.softwaremaestro.presenter.chat_page.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.classroom.entity.TutoringInfoVO
import org.softwaremaestro.presenter.databinding.ItemTutoringListTeacherBinding

class ChatListTeacherAdapter : RecyclerView.Adapter<ChatListTeacherAdapter.ViewHolder>() {

    private var items: List<TutoringInfoVO> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListTeacherAdapter.ViewHolder {
        val view = ItemTutoringListTeacherBinding.inflate(
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

    fun setItem(items: List<TutoringInfoVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTutoringListTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TutoringInfoVO) {
            binding.tvName.text = item.teacherId
        }
    }
}
package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.following_get.entity.FollowingGetResponseVO
import org.softwaremaestro.presenter.databinding.ItemMyTeacherBinding

class FollowingAdapter(private val onItemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    private var items: List<FollowingGetResponseVO> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingAdapter.ViewHolder {
        val view = ItemMyTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowingAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<FollowingGetResponseVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemMyTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: FollowingGetResponseVO) {
            binding.tvName.text = item.name
            binding.ivPhoto.setOnClickListener {
                item.id?.let { onItemClickListener(it) }
            }
        }
    }
}
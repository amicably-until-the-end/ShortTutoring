package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import org.softwaremaestro.presenter.databinding.ItemTeacherFollowingBinding
import org.softwaremaestro.presenter.util.disableFor

class TeacherFollowingAdapter(private val onItemClickListener: (FollowingGetResponseVO) -> Unit) :
    RecyclerView.Adapter<TeacherFollowingAdapter.ViewHolder>() {

    private var items: List<FollowingGetResponseVO> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherFollowingAdapter.ViewHolder {
        val view =
            ItemTeacherFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherFollowingAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<FollowingGetResponseVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTeacherFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: FollowingGetResponseVO) {
            binding.tvName.text = item.name
            Glide.with(binding.root.context).load(item.profileImage).centerCrop()
                .into(binding.ivPhoto)
            binding.ivPhoto.setOnClickListener {
                item.id?.let { onItemClickListener(item) }
                binding.ivPhoto.disableFor(500L)
            }
        }
    }
}
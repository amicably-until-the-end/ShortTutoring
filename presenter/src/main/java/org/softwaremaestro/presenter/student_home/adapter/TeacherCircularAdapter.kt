package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.databinding.ItemTeacherCircularBinding

class TeacherCircularAdapter(private val onItemClickListener: (TeacherVO) -> Unit) :
    RecyclerView.Adapter<TeacherCircularAdapter.ViewHolder>() {

    private var items: List<TeacherVO> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherCircularAdapter.ViewHolder {
        val view =
            ItemTeacherCircularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherCircularAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<TeacherVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTeacherCircularBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TeacherVO) {
            binding.tvName.text = item.nickname
            Glide.with(binding.root.context).load(item.profileUrl).centerCrop()
                .into(binding.ivPhoto)
            binding.ivPhoto.setOnClickListener {
                item.teacherId?.let { onItemClickListener(item) }
            }
        }
    }
}
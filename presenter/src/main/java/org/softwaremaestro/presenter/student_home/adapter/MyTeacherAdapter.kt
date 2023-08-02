package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.databinding.ItemLectureBinding
import org.softwaremaestro.presenter.databinding.ItemMyTeacherBinding
import org.softwaremaestro.presenter.student_home.item.Lecture
import org.softwaremaestro.presenter.student_home.item.MyTeacher

class MyTeacherAdapter(private val onItemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<MyTeacherAdapter.ViewHolder>() {

    private var items: List<MyTeacher> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTeacherAdapter.ViewHolder {
        val view = ItemMyTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyTeacherAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<MyTeacher>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemMyTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MyTeacher) {
            binding.tvName.text = item.nickname
        }
    }
}
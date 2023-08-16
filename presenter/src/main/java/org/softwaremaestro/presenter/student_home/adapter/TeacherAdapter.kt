package org.softwaremaestro.presenter.student_home.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.databinding.ItemTeacherBinding
import org.softwaremaestro.presenter.student_home.item.BestTeacher

class TeacherAdapter(private val onItemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<TeacherAdapter.ViewHolder>() {

    private var items: List<BestTeacher> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherAdapter.ViewHolder {
        val view =
            ItemTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return if (items.size >= 3) 3 else items.size
    }

    fun setItem(items: List<BestTeacher>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: BestTeacher) {

            with(binding) {
                ivProfile.setImageURI(Uri.parse(item.profileUrl))
                tvName.text = "${item.nickname} 선생님"
                tvUniv.text = item.univ
                tvPickCount.text = "찜한 학생 ${item.pickCount}명"
                tvRating.text = "%.1f".format(item.rating)
            }

            itemView.setOnClickListener {
//                item.id?.let { onItemClickListener(it) }
            }
        }
    }
}
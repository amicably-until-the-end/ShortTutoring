package org.softwaremaestro.presenter.student_home.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.databinding.ItemTeacherSimpleBinding
import java.lang.Integer.min

class TeacherAdapter(
    private val itemCountLimit: Int? = null,
    private val onItemClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<TeacherAdapter.ViewHolder>() {

    private var items: List<TeacherVO> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherAdapter.ViewHolder {
        val view =
            ItemTeacherSimpleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return if (itemCountLimit == null) items.size
        else min(itemCountLimit, items.size)
    }

    fun setItem(items: List<TeacherVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTeacherSimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TeacherVO) {

            with(binding) {
                ivProfile.setImageURI(Uri.parse(item.profileUrl))
                tvTeacherName.text = item.nickname
                tvTeacherUniv.text = item.univ
                tvTeacherBio.text = item.bio
                tvRating.text = item.rating.toString()
                tvFollowCnt.text = item.pickCount.toString()
            }

            itemView.setOnClickListener {
                item.teacherId?.let { onItemClickListener(it) }
            }
        }
    }
}
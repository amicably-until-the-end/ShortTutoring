package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.teacher_get.entity.TeacherVO
import org.softwaremaestro.presenter.databinding.ItemTeacherSimpleBinding
import org.softwaremaestro.presenter.util.disableFor
import java.lang.Integer.min

class TeacherSimpleAdapter(
    private val itemCountLimit: Int? = null,
    private val onItemClickListener: (TeacherVO) -> Unit
) :
    RecyclerView.Adapter<TeacherSimpleAdapter.ViewHolder>() {

    private var items: List<TeacherVO> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherSimpleAdapter.ViewHolder {

        val binding = ItemTeacherSimpleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TeacherSimpleAdapter.ViewHolder, position: Int) {
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

                Glide.with(root.context).load(item.profileUrl)
                    .centerCrop()
                    .into(ivProfile)
                tvTeacherName.text = item.nickname
                tvTeacherUniv.text = item.univ
                tvTeacherBio.text = item.bio
                tvRating.text = item.rating.toString()
                tvFollowCnt.text = item.pickCount.toString()
            }

            itemView.setOnClickListener {
                onItemClickListener(item)
                itemView.disableFor(500L)
            }
        }
    }
}
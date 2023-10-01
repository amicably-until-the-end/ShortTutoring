package org.softwaremaestro.presenter.teacher_search.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import org.softwaremaestro.presenter.databinding.ItemTeacherBinding
import java.lang.Integer.min

class TeacherAdapter(
    private val itemCountLimit: Int? = null,
    private val onItemClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<TeacherAdapter.ViewHolder>() {

    private var items: List<FollowingGetResponseVO> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherAdapter.ViewHolder {

        val binding = ItemTeacherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TeacherAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return if (itemCountLimit == null) items.size
        else min(itemCountLimit, items.size)
    }

    fun setItem(items: List<FollowingGetResponseVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: FollowingGetResponseVO) {

            with(binding) {
                item.profileImage?.let { ivTeacherImg.setImageURI(Uri.parse(it)) }
                tvTeacherName.text = item.name
                tvTeacherUniv.text = item.schoolName
                tvTeacherBio.text = item.bio
//                tvTeacherRating.text = item.rating.toString()
                btnFollow.text = "찜한 학생 ${item.followersCount}"
            }

            itemView.setOnClickListener {
                item.id?.let { onItemClickListener(it) }
            }
        }
    }
}
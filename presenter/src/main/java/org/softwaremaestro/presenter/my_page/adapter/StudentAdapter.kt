package org.softwaremaestro.presenter.my_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.follow.entity.FollowerGetResponseVO
import org.softwaremaestro.presenter.databinding.ItemStudentBinding

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    private var items: List<FollowerGetResponseVO> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<FollowerGetResponseVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: FollowerGetResponseVO) {

            with(binding) {
                Glide.with(root).load(item.profileImage).centerCrop().into(ivProfile)
                item.name?.let { tvNickname.text = "${it}" }
                if (item.schoolLevel != null && item.grade != null) {
                    tvStudentInfo.text = "${item.schoolLevel} ${item.grade}학년"
                }
//                tvRecentDate.text = "${item.recentDate}"
            }
        }
    }
}
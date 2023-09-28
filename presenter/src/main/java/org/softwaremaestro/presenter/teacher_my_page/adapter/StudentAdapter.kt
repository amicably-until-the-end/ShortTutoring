package org.softwaremaestro.presenter.teacher_my_page.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.follower_get.entity.FollowerGetResponseVO
import org.softwaremaestro.presenter.databinding.ItemStudentBinding

class StudentAdapter() : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    private var items: List<FollowerGetResponseVO> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentAdapter.ViewHolder {
        val view =
            ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentAdapter.ViewHolder, position: Int) {
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
                ivProfile.setImageURI(Uri.parse(item.profileImage))
                tvNickname.text = "${item.name}"
                tvGrade.text = "${item.grade}"
//                tvRecentDate.text = "${item.recentDate}"
            }
        }
    }
}
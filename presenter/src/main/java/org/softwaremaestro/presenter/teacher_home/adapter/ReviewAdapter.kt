package org.softwaremaestro.presenter.teacher_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.review.entity.ReviewResVO
import org.softwaremaestro.presenter.databinding.ItemReviewBinding
import org.softwaremaestro.presenter.util.Util.toLocalDateTime


private const val EMPTY_STRING = "-"

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var items: List<ReviewResVO> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        val view = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<ReviewResVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ReviewResVO) {

            with(binding) {
                tvName.text = item.studentName ?: EMPTY_STRING
                tvGrade.text = "${item.schoolLevel} ${item.schoolGrade}학년"
                val rating = item.reviewRating?.let { "%.01f".format(it) } ?: EMPTY_STRING
                tvRating.text = rating
                item.endedAt?.let {
                    val dT = toLocalDateTime(it)
                    tvCreatedAt.text = "${dT.year}.${dT.monthValue}.${dT.dayOfMonth}"
                }
                tvComment.text = item.reviewComment ?: EMPTY_STRING
                Glide.with(root.context).load(item.profileImage).centerCrop().into(ivProfileImage)
            }
        }
    }
}
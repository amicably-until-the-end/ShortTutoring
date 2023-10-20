package org.softwaremaestro.presenter.teacher_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.review.entity.ReviewResVO
import org.softwaremaestro.presenter.databinding.ItemReviewBinding

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
                tvCreatedAt.text = item.endedAt ?: EMPTY_STRING
                tvComment.text = item.reviewComment ?: EMPTY_STRING
            }
        }
    }
}
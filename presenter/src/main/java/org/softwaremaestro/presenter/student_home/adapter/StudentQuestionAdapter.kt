package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.question_get.entity.QuestionGetResponseVO
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

private const val EMPTY_STRING = "-"

class StudentQuestionAdapter(
    private val onItemClickListener: (QuestionGetResponseVO) -> Unit,
) : ListAdapter<QuestionGetResponseVO, StudentQuestionAdapter.ViewHolder>(QuestionDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    inner class ViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionGetResponseVO) {

            with(binding) {

                Glide.with(root.context).load(item.mainImage)
                    .centerCrop()
                    .into(ivPhoto)

                tvDesciption.text = item.problemDescription ?: EMPTY_STRING
                tvSubject.text = item.problemSubject ?: EMPTY_STRING
                ivCheck.visibility = View.GONE

                when (item.status) {
                    "pending" -> {
                        containerTime.visibility = View.GONE
                    }

                    else -> {
                        containerTime.visibility = View.VISIBLE
                        tvTime.visibility = View.VISIBLE
//                        val ldt = toLocalDateTime(item.reservedTime)
//                        val date = if (ldt == LocalDateTime.now()) "오늘"
//                        else if (ldt == LocalDateTime.now().plusDays(1L)) "내일"
//                        else "${ldt.monthValue}월 ${ldt.dayOfMonth}일"
//                        val time = ldt.format(DateTimeFormatter.ofPattern("hh:mm")
//                        tvTimeText.text = date + time
                    }
                }

                root.setOnClickListener {
                    if (item.id != null && item.images != null) {
                        onItemClickListener(item)
                    }
                }
            }
        }

    }
}

object QuestionDiffUtil : DiffUtil.ItemCallback<QuestionGetResponseVO>() {

    override fun areItemsTheSame(
        oldItem: QuestionGetResponseVO,
        newItem: QuestionGetResponseVO
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: QuestionGetResponseVO,
        newItem: QuestionGetResponseVO
    ): Boolean {
        return oldItem.id == newItem.id
    }
}
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
import org.softwaremaestro.presenter.util.Util.toLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
                tvSchoolLevelAndSubject.text =
                    if (item.problemSchoolLevel != null && item.problemSubject != null) {
                        "${item.problemSchoolLevel} ${item.problemSubject}"
                    } else EMPTY_STRING
                ivCheck.visibility = View.GONE

                when (item.status) {
                    "pending" -> {
                        containerTime.visibility = View.GONE
                    }

                    else -> {
                        containerTime.visibility = View.VISIBLE
                        tvTime.visibility = View.VISIBLE
                        item.reservedStart?.let { setTimeText(it) }
                    }
                }

                root.setOnClickListener {
                    if (item.id != null && item.images != null) {
                        onItemClickListener(item)
                    }
                }
            }
        }

        private fun setTimeText(reservedStart: String) {
            val ldt = toLocalDateTime(reservedStart)
            val date = if (ldt.dayOfMonth == LocalDateTime.now().dayOfMonth) "오늘"
            else if (ldt.dayOfMonth == LocalDateTime.now().plusDays(1L).dayOfMonth) "내일"
            else "${ldt.monthValue}. ${ldt.dayOfMonth}"
            val time = ldt.format(DateTimeFormatter.ofPattern("hh:mm"))
            binding.tvTimeText.text = "$date $time"
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
}
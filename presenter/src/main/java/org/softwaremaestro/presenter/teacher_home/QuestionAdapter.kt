package org.softwaremaestro.presenter.teacher_home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.indices
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

class QuestionAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    private var items: List<QuestionGetResultVO> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAdapter.ViewHolder {
        val view = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<QuestionGetResultVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: QuestionGetResultVO) {
            // Todo: 수정 필요함
            binding.ivPhoto.setImageBitmap(null)
            binding.tvSubject.text = item.problemSchoolSubject
            binding.tvChapter.text = item.problemSchoolChapter
            binding.tvDifficulty.text = item.problemDifficulty
            binding.tvDesciption.text = item.problemDescription
            for (i in 0..2) {
                if (i in item.reviews.indices && i in binding.containerReview.indices) {
                    (binding.containerReview.getChildAt(i) as TextView).text = item.reviews[i]
                }
            }

            binding.btnAnswer.setOnClickListener {
                listener.onItemClick()
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClick()
}
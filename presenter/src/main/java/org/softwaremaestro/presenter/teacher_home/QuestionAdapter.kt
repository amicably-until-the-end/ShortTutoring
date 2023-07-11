package org.softwaremaestro.presenter.teacher_home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.indices
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

private const val EMPTY_STRING = "-"

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
            binding.tvSubject.text = item.problemSchoolSubject ?: EMPTY_STRING
            binding.tvChapter.text = item.problemSchoolChapter ?: EMPTY_STRING
            binding.tvDifficulty.text = item.problemDifficulty ?: EMPTY_STRING
            binding.tvDesciption.text = item.problemDescription ?: EMPTY_STRING
            for (i in 0..2) {
                (binding.containerReview.getChildAt(i) as TextView).text = item.reviews?.get(i) ?: EMPTY_STRING
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
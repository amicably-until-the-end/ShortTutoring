package org.softwaremaestro.presenter.teacher_home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

private const val EMPTY_STRING = "-"

class QuestionAdapter(private val answerBtnClickListener: (String) -> Unit) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

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

    inner class ViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: QuestionGetResultVO) {

            // Todo: 수정 필요함
            binding.ivPhoto.setImageBitmap(null)
            binding.tvSubject.text = item.problemSchoolSubject ?: EMPTY_STRING
            binding.tvDifficulty.text = when (item.problemDifficulty) {
                "Difficult" -> "어려움"
                "Midium" -> "중간"
                "Easy" -> "쉬움"
                else -> EMPTY_STRING
            }
            binding.tvDesciption.text = item.problemDescription ?: EMPTY_STRING

            binding.root.setOnClickListener {
                item.id?.let { answerBtnClickListener(it) }
            }
        }
    }
}
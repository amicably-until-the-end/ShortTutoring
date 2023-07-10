package org.softwaremaestro.presenter.teacher_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

class QuestionAdapter(val items: List<QuestionGetResultVO>, val listener: OnItemClickListener): RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

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

    inner class ViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: QuestionGetResultVO) {
            // Todo: 수정 필요함
            binding.ivPhoto.setImageBitmap(null)
            binding.tvSubject.text = item.problemSchoolSubject
            binding.tvChapter.text = item.problemSchoolChapter
            binding.tvDifficulty.text = item.problemDifficulty
            binding.tvDesciption.text = item.problemDescription
            item.reviews[0]?.let { binding.tvReview1.text = it }
            item.reviews[1]?.let { binding.tvReview2.text = it }
            item.reviews[2]?.let { binding.tvReview3.text = it }
            binding.btnAnswer.setOnClickListener {
                listener.onItemClick()
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClick()
}
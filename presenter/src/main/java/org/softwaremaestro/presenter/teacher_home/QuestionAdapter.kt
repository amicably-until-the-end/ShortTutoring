package org.softwaremaestro.presenter.teacher_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.model.vo.QuestionVO
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding

class QuestionAdapter(val items: List<QuestionVO>, val listener: OnItemClickListener): RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

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

        fun onBind(item: QuestionVO) {
            // Todo: 수정 필요함
            binding.ivPhoto.setImageBitmap(null)
            binding.tvSubject.text = item.subject.toString()
            binding.tvTime.text = item.time.toString()
            binding.tvGrade.text = item.grade.toString()
            binding.tvReview1.text = item.reviews[0].toString()
            binding.tvReview2.text = item.reviews[1].toString()
            binding.tvReview3.text = item.reviews[2].toString()
            binding.btnAnswer.setOnClickListener {
                listener.onItemClick()
            }
        }
    }
}

interface OnItemClickListener {
    fun onItemClick()
}
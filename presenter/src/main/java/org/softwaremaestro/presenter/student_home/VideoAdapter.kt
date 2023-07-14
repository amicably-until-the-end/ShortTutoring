package org.softwaremaestro.presenter.student_home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.presenter.databinding.ItemQuestionBinding
import org.softwaremaestro.presenter.databinding.ItemTutoringBinding

private const val EMPTY_STRING = "-"

class TutoringAdapter(private val onItemClickListener: (String) -> Unit): RecyclerView.Adapter<TutoringAdapter.ViewHolder>() {

    private var items: List<Tutoring> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutoringAdapter.ViewHolder {
        val view = ItemTutoringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TutoringAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<Tutoring>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTutoringBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Tutoring) {

            // Todo: 수정 필요함
            binding.ivPhoto.setImageBitmap(null)
            binding.tvDesciption.text = item.description
            binding.tvDate.text = item.date
            binding.root.setOnClickListener {
                onItemClickListener("string from ViewAdapter")
            }
        }
    }
}
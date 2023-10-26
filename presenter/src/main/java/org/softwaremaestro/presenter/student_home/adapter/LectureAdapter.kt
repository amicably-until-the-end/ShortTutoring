package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.softwaremaestro.domain.tutoring_get.entity.TutoringVO
import org.softwaremaestro.presenter.databinding.ItemLectureBinding
import org.softwaremaestro.presenter.util.Util.toLocalDateTime
import java.time.LocalDateTime

class LectureAdapter(private val onItemClickListener: (TutoringVO) -> Unit) :
    RecyclerView.Adapter<LectureAdapter.ViewHolder>() {

    private var items: List<TutoringVO> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureAdapter.ViewHolder {
        val view = ItemLectureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<TutoringVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemLectureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TutoringVO) {

            Glide.with(binding.root.context).load(item.questionImage).centerCrop()
                .into(binding.ivThumbNail)
            binding.tvDesciption.text = item.description
            binding.tvSubject.text = item.schoolSubject
            binding.root.setOnClickListener {
                onItemClickListener(item)
            }
            item.tutoringDate?.let { tDate ->
                val ldt = toLocalDateTime(tDate)
                val today = LocalDateTime.now().withHour(0).withMinute(0)
                val date = if (ldt > today) "오늘"
                else if (ldt > today.plusDays(-1L)) "어제"
                else "${ldt.monthValue}월 ${ldt.dayOfMonth}일"
                binding.tvTime.text = date
            }
        }
    }
}
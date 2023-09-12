package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.review_get.LectureVO
import org.softwaremaestro.presenter.databinding.ItemLectureBinding

class LectureAdapter(private val onItemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<LectureAdapter.ViewHolder>() {

    private var items: List<LectureVO> = emptyList()

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

    fun setItem(items: List<LectureVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemLectureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: LectureVO) {

            // Todo: 수정 필요함
            binding.ivPhoto.setImageBitmap(null)
            binding.tvDesciption.text = item.description
            binding.tvSubject.text = item.subject
            binding.root.setOnClickListener {
                // 영상을 재생하기 위해 listener에 url을 넘긴다
                onItemClickListener("example url")
            }
        }
    }
}
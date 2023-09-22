package org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import org.softwaremaestro.presenter.databinding.ItemTeacherSelectableBinding

private const val EMPTY_STRING = "-"

class TeacherSelectAdapter(
    private var items: List<TeacherVO>,
    private val listener: (String) -> Unit
) :
    RecyclerView.Adapter<TeacherSelectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            ItemTeacherSelectableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<TeacherVO>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemTeacherSelectableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TeacherVO) {
            binding.tvLikes.text = " · 찜한 사람 ${item.likes}명" ?: EMPTY_STRING
            binding.tvTeacherName.text = item.teacherId ?: EMPTY_STRING
            binding.tvTeacherSchool.text = item.school ?: EMPTY_STRING

            item.teacherId?.let { teacherId ->
                binding.btnAccept.setOnClickListener {
                    listener(teacherId)

                }
            }
        }
    }
}
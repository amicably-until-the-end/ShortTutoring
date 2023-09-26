package org.softwaremaestro.presenter.chat_page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemTeacherSelectableBinding

class TeacherSelectAdapter(
    private var items: List<TeacherVO>,
    private val listener: (String) -> Unit
) :
    RecyclerView.Adapter<TeacherSelectAdapter.ViewHolder>() {

    private var selectedView: ItemTeacherSelectableBinding? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherSelectAdapter.ViewHolder {
        val view =
            ItemTeacherSelectableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherSelectAdapter.ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<TeacherVO>) {
        this.items = items
    }

    private fun setSelectedView(currentSelect: ItemTeacherSelectableBinding) {
        selectedView?.apply {
            cvInfoBox.strokeColor = root.context.getColor(R.color.background_grey)
            btnSelect.visibility = View.GONE
//            containerReviewAndClip.visibility = View.GONE
        }

        currentSelect.apply {
            cvInfoBox.strokeColor = root.context.getColor(R.color.primary_blue)
            btnSelect.visibility = View.VISIBLE
//            containerReviewAndClip.visibility = View.VISIBLE
        }

        selectedView = currentSelect
    }

    inner class ViewHolder(private val binding: ItemTeacherSelectableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TeacherVO) {
            with(binding) {
                tvFollowCnt.text = item.likes?.toString() ?: EMPTY_STRING
                tvTeacherName.text = item.teacherId ?: EMPTY_STRING
                tvTeacherUniv.text = item.school ?: EMPTY_STRING
                if (selectedView == null) {
                    setSelectedView(binding)
                }
                root.setOnClickListener {
                    setSelectedView(binding)
                }
            }
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}
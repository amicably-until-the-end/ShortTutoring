package org.softwaremaestro.presenter.chat_page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemAnsweringTeacherSelectBinding

class TeacherSelectAdapter(
    private var items: List<TeacherVO>,
    private val listener: (String) -> Unit
) :
    RecyclerView.Adapter<TeacherSelectAdapter.ViewHolder>() {

    private var selectedView: ItemAnsweringTeacherSelectBinding? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherSelectAdapter.ViewHolder {
        val view =
            ItemAnsweringTeacherSelectBinding.inflate(
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

    private fun setSelectedView(currentSelect: ItemAnsweringTeacherSelectBinding) {
        selectedView?.cvRoot?.strokeColor =
            selectedView?.root?.context?.getColor(R.color.background_grey)!!
        selectedView?.btnSelect?.visibility = View.GONE
        currentSelect.cvRoot.strokeColor = currentSelect.root.context.getColor(R.color.primary_blue)
        currentSelect.btnSelect.visibility = View.VISIBLE

        selectedView = currentSelect
    }

    inner class ViewHolder(private val binding: ItemAnsweringTeacherSelectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TeacherVO) {
            binding.apply {
                tvLikes.text = "♥ ${item.likes ?: 0}"
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
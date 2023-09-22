package org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.databinding.ItemDesiredClassTimeBinding
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog

class TimeSelectAdapter(private val onAddClick: () -> Unit) :
    RecyclerView.Adapter<TimeSelectAdapter.ViewHolder>() {

    var items: MutableList<TimePickerBottomDialog.SpecificTime> = mutableListOf()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            ItemDesiredClassTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < items.size) {
            holder.onBind(items[position])
        } else {
            holder.onBind(null)
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    inner class ViewHolder(private val binding: ItemDesiredClassTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TimePickerBottomDialog.SpecificTime?) {
            if (item != null) {
                binding.tvTime.text = item.toString()
                binding.tvAddTime.visibility = ViewGroup.GONE
                binding.root.setOnClickListener {
                    items.remove(item)
                    notifyDataSetChanged()
                }
            } else {
                binding.tvTime.text = ""
                binding.tvAddTime.visibility = ViewGroup.VISIBLE
                binding.root.setOnClickListener {
                    onAddClick()
                }
            }
        }
    }

}
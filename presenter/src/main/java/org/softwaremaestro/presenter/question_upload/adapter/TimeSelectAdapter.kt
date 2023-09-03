package org.softwaremaestro.presenter.question_upload.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.databinding.ItemDesiredClassTimeBinding

class TimeSelectAdapter() : RecyclerView.Adapter<TimeSelectAdapter.ViewHolder>() {


    var items: List<String> = emptyList()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeSelectAdapter.ViewHolder {
        val view =
            ItemDesiredClassTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSelectAdapter.ViewHolder, position: Int) {
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

        fun onBind(item: String?) {
            if (item != null) {
                binding.tvTime.text = item
            } else {
                binding.tvAddTime.visibility = ViewGroup.VISIBLE
                binding.root.setOnClickListener {
                    binding.root.isEnabled = false
                }
            }
        }

    }

}
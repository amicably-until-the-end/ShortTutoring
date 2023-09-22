package org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.databinding.ItemCapturePreviewBinding

class CapturePreviewAdapter(private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<CapturePreviewAdapter.ViewHolder>() {

    var items: MutableList<Bitmap> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            ItemCapturePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < items.size) {
            holder.onBind(items[position], position)
        } else {
            holder.onBind(null, position)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    fun setItem(items: MutableList<Bitmap>) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemCapturePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: Bitmap?, position: Int) {
            binding.ivCapture.setImageBitmap(item)
            binding.root.setOnClickListener {
                onClick(position)

            }
        }
    }

}
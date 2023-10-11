package org.softwaremaestro.presenter.student_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.softwaremaestro.domain.event.entity.EventVO
import org.softwaremaestro.domain.event.entity.EventsVO
import org.softwaremaestro.presenter.databinding.ItemEventBinding

class EventAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private var items: EventsVO = EventsVO(0, emptyList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val view = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        items.events?.let { holder.onBind(it[position]) }
    }

    override fun getItemCount(): Int {
        return items.events?.size ?: 0
    }

    fun setItems(items: EventsVO) {
        this.items = items
    }

    inner class ViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: EventVO) {
            Glide.with(binding.root.context).load(item.image)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(20)
                    )
                )
                .into(binding.ivEvent)
            binding.ivEvent.setOnClickListener {
                item.url?.let { onClick(it) }
            }
        }
    }
}

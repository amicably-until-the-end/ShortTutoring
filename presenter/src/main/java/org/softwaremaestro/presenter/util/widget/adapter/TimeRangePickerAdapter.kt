package org.softwaremaestro.presenter.util.widget.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemTimeRangePickerItemBinding
import java.sql.Time

class TimeRangePickerAdapter(
    private val minuteInterval: Int,
    private val onRangeChange: (TimeItem, TimeItem) -> Unit,
) :
    RecyclerView.Adapter<TimeRangePickerAdapter.ViewHolder>() {

    private lateinit var items: MutableList<TimeItem>

    private var rangeStart: TimeItem? = null
    private var rangeEnd: TimeItem? = null


    private fun initializeDateItem() {
        items = mutableListOf()
        for (hour in 0 until 24) {
            for (minute in 0 until 60 step minuteInterval) { // 10분 간격으로 생성
                val isAvailable = true // 원하는 조건을 여기에 추가할 수 있습니다.
                val timeItem = TimeItem(hour, minute, isAvailable)
                items.add(timeItem)
            }
        }
    }

    init {
        initializeDateItem()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeRangePickerAdapter.ViewHolder {
        val view =
            ItemTimeRangePickerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }


    fun getSelectedTimeRange(): Pair<TimeItem, TimeItem>? {
        if (rangeStart == null || rangeEnd == null) return null
        return Pair(rangeStart!!, rangeEnd!!)
    }

    inner class ViewHolder(private val binding: ItemTimeRangePickerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var dateItem: TimeItem

        fun onBind(item: TimeItem) {
            binding.apply {
                if (item.minute % (minuteInterval * 3) == 0) {
                    tvTimeTag.text = "${item.hour}:${String.format("%02d", item.minute)}"
                    divierLarge.visibility = ViewGroup.VISIBLE
                    divierSmall.visibility = ViewGroup.GONE
                } else {
                    tvTimeTag.text = ""
                    divierLarge.visibility = ViewGroup.GONE
                    divierSmall.visibility = ViewGroup.VISIBLE
                }
                if ((rangeStart?.equals(item) == true) && rangeEnd == null) {
                    btnSelect.background = root.context.getDrawable(R.color.secondary_blue)
                } else if (rangeEnd != null && (item >= rangeStart!! && item <= rangeEnd!!)) {
                    btnSelect.background =
                        root.context.getDrawable(R.color.primary_blue)
                } else {
                    btnSelect.background = root.context.getDrawable(R.color.background_grey)
                }

                btnSelect.setOnClickListener {
                    if (rangeStart == null || rangeStart!! > item) {
                        rangeStart = item
                        rangeEnd = null
                    } else if (rangeEnd == null && item > rangeStart!!) {
                        rangeEnd = item
                        onRangeChange(rangeStart!!, rangeEnd!!)
                    } else {
                        rangeStart = item
                        rangeEnd = null
                    }
                    Log.d("TimeRangePickerAdapter", "rangeStart: $rangeStart, rangeEnd: $rangeEnd")
                    notifyDataSetChanged()
                }

            }

            dateItem = item
        }
    }

    inner class TimeItem(
        val hour: Int,
        val minute: Int,
        val isAvailable: Boolean
    ) : Comparable<TimeItem> {
        override fun compareTo(other: TimeItem): Int {
            return if (this.hour == other.hour) this.minute - other.minute
            else this.hour - other.hour
        }

        override fun toString(): String {
            return "${hour}: ${minute}"
        }
    }


    companion object {
        private const val itemConstructBuffer = 40
    }

}

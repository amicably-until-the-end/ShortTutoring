package org.softwaremaestro.presenter.util.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemTimeRangePickerItemBinding

class TimePickerAdapter(
    private val minuteInterval: Int
) :
    RecyclerView.Adapter<TimePickerAdapter.ViewHolder>() {

    private lateinit var items: MutableList<TimeItem>

    init {
        initializeDateItem()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
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

    inner class ViewHolder(private val binding: ItemTimeRangePickerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var mItem: TimeItem
        private lateinit var mBinding: ItemTimeRangePickerItemBinding

        fun onBind(item: TimeItem) {

            mItem = item
            mBinding = binding

            setVisibility()
            setBtnSelect()
        }

        private fun setVisibility() {
            with(mBinding) {
                if ((mItem.hour * 60 + mItem.minute) % (minuteInterval * 3) == 0) {
                    tvTimeTag.text = mItem.toString()
                    dividerLarge.visibility = ViewGroup.VISIBLE
                    dividerSmall.visibility = ViewGroup.GONE
                } else {
                    tvTimeTag.text = ""
                    dividerLarge.visibility = ViewGroup.GONE
                    dividerSmall.visibility = ViewGroup.VISIBLE
                }
            }
        }

        private fun setBtnSelect() {
            mBinding.btnSelect.setOnClickListener {

                val dividerColor =
                    if (mItem.selected) Color.parseColor("#FFFFFFFF") else Color.parseColor("#FFCFD1D8")
                mBinding.dividerLarge.setBackgroundColor(dividerColor)
                mBinding.dividerSmall.setBackgroundColor(dividerColor)

                val btnColor = if (mItem.selected) R.color.background_grey else R.color.primary_blue
                mBinding.btnSelect.setBackgroundResource(btnColor)

                mItem.selected = !mItem.selected
            }
        }
    }

    inner class TimeItem(
        val hour: Int,
        val minute: Int,
        var selected: Boolean = false
    ) : Comparable<TimeItem> {

        override fun compareTo(other: TimeItem): Int {
            return if (this.hour == other.hour) this.minute - other.minute
            else this.hour - other.hour
        }

        override fun toString(): String {
            return "${hour}:${"%02d".format(minute)}"
        }
    }

    private fun initializeDateItem() {
        items = mutableListOf()
        for (hour in 0 until 24) {
            for (minute in 0 until 60 step minuteInterval) { // 10분 간격으로 생성
                val isAvailable = true // 원하는 조건을 여기에 추가할 수 있습니다.
                val timeItem = TimeItem(hour, minute)
                items.add(timeItem)
            }
        }
    }
}

package org.softwaremaestro.presenter.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemTimeRangePickerItemBinding

class TimeRangePickerAdapter(
    private val minuteInterval: Int,
    private val onBtnClick: (TimeItem?, TimeItem?) -> Unit,
    private val onRangeChange: (TimeItem, TimeItem) -> Unit,
) :
    RecyclerView.Adapter<TimeRangePickerAdapter.ViewHolder>() {

    private lateinit var items: MutableList<TimeItem>

    var rangeStart: TimeItem? = null
    var rangeEnd: TimeItem? = null


    private fun initializeDateItem() {
        items = mutableListOf()
        for (hour in 0 until 24) {
            for (minute in 0 until 60 step minuteInterval) { // 10분 간격으로 생성
                val isAvailable = true // 원하는 조건을 여기에 추가할 수 있습니다.
//                val mHour = (LocalDateTime.now().hour + hour) % 24
//                val mMinute = ((LocalDateTime.now().minute / 10 * 10) + minute) % 60
//                val timeItem = TimeItem(mHour, mMinute, isAvailable)

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


    fun getSelectedTimeRange(): Pair<TimeItem, TimeItem>? {
        if (rangeStart == null || rangeEnd == null) return null
        return Pair(rangeStart!!, rangeEnd!!)
    }

    inner class ViewHolder(private val binding: ItemTimeRangePickerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var mItem: TimeItem
        private lateinit var mBinding: ItemTimeRangePickerItemBinding

        fun onBind(item: TimeItem) {

            mItem = item
            mBinding = binding

            setVisibility()
            setColor()
            setBtnSelect()
        }

        private fun setVisibility() {
            with(mBinding) {
                if (mItem.minute % (minuteInterval * 3) == 0) {
                    tvTimeTag.text = "${mItem.hour}:${"%02d".format(mItem.minute)}"
                    dividerLarge.visibility = ViewGroup.VISIBLE
                    dividerSmall.visibility = ViewGroup.GONE
                } else {
                    tvTimeTag.text = ""
                    dividerLarge.visibility = ViewGroup.GONE
                    dividerSmall.visibility = ViewGroup.VISIBLE
                }
            }
        }

        private fun setColor() {
            with(mBinding) {
                if ((rangeStart?.equals(mItem) == true) && rangeEnd == null) {
                    btnSelect.background = root.context.getDrawable(R.color.secondary_blue)
                } else if (rangeEnd != null && (mItem >= rangeStart!! && mItem <= rangeEnd!!)) {
                    btnSelect.background =
                        root.context.getDrawable(R.color.primary_blue)
                } else {
                    btnSelect.background = root.context.getDrawable(R.color.background_grey)
                }
            }
        }

        private fun setBtnSelect() {
            mBinding.btnSelect.setOnClickListener {
                if (rangeStart == null || rangeStart!! > mItem) {
                    rangeStart = mItem
                    rangeEnd = null
                } else if (rangeEnd == null && mItem > rangeStart!!) {
                    rangeEnd = mItem
                    onRangeChange(rangeStart!!, rangeEnd!!)
                } else {
                    rangeStart = mItem
                    rangeEnd = null
                }
                onBtnClick(rangeStart, rangeEnd)
                notifyDataSetChanged()
            }
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
            return "${"%2d".format(hour)}:${"%02d".format(minute)}"
        }

        fun toTime() = hour * 60 + minute

        fun plusMinute(i: Int): TimeRangePickerAdapter.TimeItem {
            return if (minute + i >= 60) {
                TimeItem(hour + 1, minute - (60 - i), isAvailable)
            } else {
                TimeItem(hour, minute + i, isAvailable)
            }
        }
    }


    companion object {
        private const val itemConstructBuffer = 40
    }

}

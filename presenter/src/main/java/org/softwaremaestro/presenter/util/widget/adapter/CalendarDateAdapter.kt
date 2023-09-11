package org.softwaremaestro.presenter.util.widget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemCalendarDayBinding
import java.lang.Integer.max
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class CalendarDateAdapter(
    private val recyclerView: RecyclerView,
    private val onSelectedDayChange: (Int, Int, Int) -> Unit,
    private val onMonthChange: (Int, Int) -> Unit
) :
    RecyclerView.Adapter<CalendarDateAdapter.ViewHolder>() {

    private lateinit var items: MutableList<LocalDate>

    private var lastShowedItemPosition: Int = 0

    var selectedHolder: CalendarDateAdapter.ViewHolder? = null

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (lastShowedItemPosition > items.size - itemConstructBuffer) {
                addFollowingTwoMonth()
                notifyDataSetChanged()
            }
        }
    }

    init {
        initializeDateItem()
        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarDateAdapter.ViewHolder {
        val view =
            ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    private fun addMonthItems(year: Int, month: Int) {

        val daysOfMonth = LocalDate.of(year, month, 1).lengthOfMonth()
        (1..daysOfMonth).forEach {
            items.add(LocalDate.of(year, month, it))
        }
    }

    private fun addFollowingMonthItem() {
        val lastItem = items.last()
        val nextYear = lastItem.year + (if (lastItem.monthValue == 12) 1 else 0)
        val nextMonth = if (lastItem.monthValue == 12) 1 else (lastItem.monthValue + 1)
        addMonthItems(nextYear, nextMonth)
    }


    private fun addFollowingTwoMonth() {
        (0..2).forEach { _ ->
            addFollowingMonthItem()
        }
    }

    private fun initializeDateItem() {
        items = mutableListOf()

        val today = LocalDate.now()
        val month = today.monthValue
        val startOfWeek = if (today.dayOfWeek == DayOfWeek.SUNDAY) today else today.minusWeeks(1)
            .with(DayOfWeek.SUNDAY)
        while (startOfWeek.monthValue != month) {
            items.add(
                LocalDate.of(
                    startOfWeek.year,
                    startOfWeek.monthValue,
                    startOfWeek.dayOfMonth
                )
            )
            startOfWeek.plusDays(1)
        }
        (startOfWeek.dayOfMonth..startOfWeek.lengthOfMonth()).forEach {
            items.add(
                LocalDate.of(
                    startOfWeek.year,
                    startOfWeek.monthValue,
                    it
                )
            )
        }
        addFollowingTwoMonth()

    }

    override fun onBindViewHolder(holder: CalendarDateAdapter.ViewHolder, position: Int) {
        lastShowedItemPosition = max(lastShowedItemPosition, position)
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.dateItem.dayOfMonth > 20) {
            onMonthChange(holder.dateItem.year, holder.dateItem.monthValue)
        }
    }

    private fun onDateClick(nowSelect: ViewHolder) {
        nowSelect.binding.apply {
            tvDate.setTextColor(
                root.context.getColor(R.color.white)
            )
            cvDateMarker.setCardBackgroundColor(
                root.context.getColor(R.color.primary_blue)
            )
        }
        if (selectedHolder != null) {
            selectedHolder!!.binding.apply {
                cvDateMarker?.setCardBackgroundColor(
                    if (selectedHolder?.dateItem?.compareTo(LocalDate.now()) == 0) root.context.getColor(
                        R.color.background_grey
                    ) else root.context.getColor(R.color.transparent)
                )
                tvDate?.setTextColor(
                    root.context.getColor(R.color.black)
                )
            }
        }
        selectedHolder = nowSelect
        nowSelect.dateItem.let { it ->
            onSelectedDayChange(it.year, it.monthValue, it.dayOfMonth)
        }
        onMonthChange(nowSelect.dateItem.year, nowSelect.dateItem.monthValue)
    }

    inner class ViewHolder(val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var dateItem: LocalDate

        fun onBind(item: LocalDate) {
            dateItem = item
            binding.tvDate.text = item.dayOfMonth.toString()
            if (item.compareTo(LocalDate.now()) < 0) {
                binding.tvDate.setTextColor(binding.root.context.getColor(R.color.grey))
                binding.root.isEnabled = false
            } else {
                binding.tvDate.setTextColor(binding.root.context.getColor(R.color.black))
                binding.root.isEnabled = true
            }
            if (LocalDate.now().compareTo(item) == 0) {
                binding.cvDateMarker.setCardBackgroundColor(binding.root.context.getColor(R.color.background_grey))
                binding.tvToday.visibility = ViewGroup.VISIBLE
            } else {
                binding.tvToday.visibility = ViewGroup.INVISIBLE
                binding.cvDateMarker.setCardBackgroundColor(binding.root.context.getColor(R.color.transparent))
            }
            binding.root.setOnClickListener {
                onDateClick(this)
            }
        }
    }


    companion object {
        private const val itemConstructBuffer = 40
    }

}

package org.softwaremaestro.presenter.Util.Widget.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.Util.Widget.SimpleDatePicker
import org.softwaremaestro.presenter.databinding.ItemCalendarDayBinding
import org.softwaremaestro.presenter.databinding.ItemQuestionFormImageBinding
import java.lang.Integer.max
import java.time.DayOfWeek
import java.time.LocalDate

class CalendarDateAdapter(
    private val recyclerView: RecyclerView,
    private val onClick: (DateItem) -> Unit,
    private val onMonthChange: (Int, Int) -> Unit
) :
    RecyclerView.Adapter<CalendarDateAdapter.ViewHolder>() {

    private lateinit var items: MutableList<DateItem>

    private var lastShowedItemPosition: Int = 0

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
            items.add(DateItem(year, month, it))
        }
    }

    private fun addFollowingMonthItem() {
        val lastItem = items.last()
        val nextYear = lastItem.year + (if (lastItem.month == 12) 1 else 0)
        val nextMonth = if (lastItem.month == 12) 1 else (lastItem.month + 1)
        addMonthItems(nextYear, nextMonth)
    }


    private fun addFollowingTwoMonth() {
        (0..2).forEach { _ ->
            addFollowingMonthItem()
        }
    }

    private fun checkAddItemNeeded(position: Int) {
        if (items.size - itemConstructBuffer < position) {
            addFollowingTwoMonth()
            //notifyDataSetChanged()
        }
    }

    private fun initializeDateItem() {
        items = mutableListOf()

        val today = LocalDate.now()
        val month = today.monthValue
        val startOfWeek = today.with(DayOfWeek.SUNDAY)
        while (startOfWeek.monthValue != month) {
            items.add(DateItem(startOfWeek.year, startOfWeek.monthValue, startOfWeek.dayOfMonth))
            startOfWeek.plusDays(1)
        }
        (today.dayOfMonth..startOfWeek.lengthOfMonth()).forEach {
            items.add(DateItem(startOfWeek.year, startOfWeek.monthValue, it))
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
        if (holder.dateItem.day > 20) {
            onMonthChange(holder.dateItem.year, holder.dateItem.month)
        }
    }

    inner class ViewHolder(private val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var dateItem: DateItem

        fun onBind(item: DateItem) {
            binding.tvDate.text = item.day.toString()
            dateItem = item
        }
    }

    inner class DateItem(
        val year: Int,
        val month: Int,
        val day: Int,
        val selected: Boolean = false,
        isToday: Boolean = false
    )


    companion object {
        private const val itemConstructBuffer = 40
    }

}

package org.softwaremaestro.presenter.util.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ItemCalendarDayBinding
import java.lang.Integer.max
import java.time.DayOfWeek
import java.time.LocalDate

class CalendarDateAdapter(
    private val defaultDate: LocalDate? = null,
    private val recyclerView: RecyclerView,
    private val onSelectedDayChange: (Int, Int, Int) -> Unit,
    private val onMonthChange: (Int, Int) -> Unit
) :
    RecyclerView.Adapter<CalendarDateAdapter.ViewHolder>() {

    private lateinit var items: MutableList<LocalDate>

    private var lastShowedItemPosition: Int = 0
    private var monthShown = LocalDate.now().monthValue

    var selectedHolder: ViewHolder? = null

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (lastShowedItemPosition > items.size - itemConstructBuffer) {
                addFollowingTwoMonth()
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        val item = items[position]
        return "${item.year}${"%02d".format(item.monthValue)}${"%02d".format(item.dayOfMonth)}".toLong()
    }

    init {
        initializeDateItem()
        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
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
        val yearOfNextMonth = lastItem.year + (if (lastItem.monthValue == 12) 1 else 0)
        val nextMonth = if (lastItem.monthValue == 12) 1 else (lastItem.monthValue + 1)
        // 현재 12월까지만 더하도록 되어있음
        if (yearOfNextMonth >= 2024) return
        addMonthItems(yearOfNextMonth, nextMonth)
    }


    private fun addFollowingTwoMonth() {
        repeat(2) {
            addFollowingMonthItem()
        }
    }

    private fun initializeDateItem() {
        items = mutableListOf()

        addDateFromStartOfThisWeekToLastOfThisMonth()
        addFollowingTwoMonth()
    }

    private fun addDateFromStartOfThisWeekToLastOfThisMonth() {

        val today = LocalDate.now()
        val thisMonth = today.monthValue
        var startOfThisWeek =
            if (today.dayOfWeek == DayOfWeek.SUNDAY) today
            else today.minusWeeks(1).with(DayOfWeek.SUNDAY)

        while (startOfThisWeek.monthValue <= thisMonth) {
            items.add(
                LocalDate.of(
                    startOfThisWeek.year,
                    startOfThisWeek.monthValue,
                    startOfThisWeek.dayOfMonth
                )
            )
            startOfThisWeek = startOfThisWeek.plusDays(1)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        lastShowedItemPosition = max(lastShowedItemPosition, position)
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val item = holder.dateItem
        if (item.dayOfMonth >= item.lengthOfMonth() - 7) {
            monthShown = item.monthValue
            onMonthChange(item.year, item.monthValue)
        }
    }

    fun onDateClick(nowSelect: ViewHolder) {
        if (selectedHolder != null) {
            with(selectedHolder!!.binding) {
                cvDateMarker.setCardBackgroundColor(
                    if (selectedHolder!!.dateItem == LocalDate.now())
                        root.context.getColor(R.color.background_grey)
                    else
                        root.context.getColor(R.color.transparent)
                )

                tvDate.setTextColor(
                    if (selectedHolder!!.dateItem.monthValue == monthShown)
                        root.context.getColor(R.color.black)
                    else
                        Color.parseColor("#FFCFD1D8")
                )
            }
        }
        selectedHolder = nowSelect

        with(nowSelect.binding) {
            tvDate.setTextColor(root.context.getColor(R.color.white))
            cvDateMarker.setCardBackgroundColor(root.context.getColor(R.color.primary_blue))
        }
        nowSelect.dateItem.let {
            onSelectedDayChange(it.year, it.monthValue, it.dayOfMonth)
        }
//        onMonthChange(nowSelect.dateItem.year, nowSelect.dateItem.monthValue)
    }

    inner class ViewHolder(val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var dateItem: LocalDate

        fun onBind(item: LocalDate) {
            dateItem = item
            binding.tvDate.text = item.dayOfMonth.toString()

            binding.root.isEnabled = LocalDate.now() <= item

            binding.tvDate.setTextColor(
                if (item.monthValue == monthShown && item >= LocalDate.now())
                    binding.root.context.getColor(R.color.black)
                else
                    Color.parseColor("#FFCFD1D8")
            )

            binding.tvToday.visibility = if (LocalDate.now() == item) VISIBLE else INVISIBLE

            binding.cvDateMarker.setCardBackgroundColor(
                if (LocalDate.now() == item)
                    binding.root.context.getColor(R.color.background_grey)
                else
                    binding.root.context.getColor(R.color.transparent)
            )

            binding.root.setOnClickListener {
                onDateClick(this)
            }

            defaultDate?.let { date ->
                if (item == date) {
                    onDateClick(this)
                }
            }
        }
    }


    companion object {
        private const val itemConstructBuffer = 40
    }

}

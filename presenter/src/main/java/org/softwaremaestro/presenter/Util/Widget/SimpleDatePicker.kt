package org.softwaremaestro.presenter.Util.Widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.Util.Widget.adapter.CalendarDateAdapter
import org.softwaremaestro.presenter.databinding.WidgetSimpleDatePickerBinding

class SimpleDatePicker(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding: WidgetSimpleDatePickerBinding =
        WidgetSimpleDatePickerBinding.inflate(LayoutInflater.from(context), this, true)

    private lateinit var adapter: CalendarDateAdapter

    var selectedDate: CalendarDateAdapter.DateItem? = null

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.SimpleDatePicker).apply {
                recycle()
            }
        }
        setDateRecyclerView()
    }

    private fun setDateRecyclerView() {
        binding.rvCalendar.apply {
            adapter = CalendarDateAdapter(this, { date -> selectedDate = date }, { year, month ->
                binding.tvMonth.text = "${year}년 ${month}월"
            })
            layoutManager = GridLayoutManager(context, 7)
            addItemDecoration(GridSpacingItemDecoration(7, 0))
        }
    }
}

internal class GridSpacingItemDecoration(
    private val spanCount: Int, // Grid의 column 수
    private val spacing: Int // 간격
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view)

        if (position >= 0) {
            val column = position % spanCount // item column
            outRect.apply {
                // spacing - column * ((1f / spanCount) * spacing)
                left = spacing - column * spacing / spanCount
                // (column + 1) * ((1f / spanCount) * spacing)
                right = (column + 1) * spacing / spanCount
                if (position < spanCount) top = spacing
                bottom = spacing
            }
        } else {
            outRect.apply {
                left = 0
                right = 0
                top = 0
                bottom = 0
            }
        }
    }
}
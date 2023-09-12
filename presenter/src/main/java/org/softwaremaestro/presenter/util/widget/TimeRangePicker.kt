package org.softwaremaestro.presenter.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.WidgetTimeRangePickerBinding
import org.softwaremaestro.presenter.util.widget.adapter.TimeRangePickerAdapter

class TimeRangePicker(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding: WidgetTimeRangePickerBinding =
        WidgetTimeRangePickerBinding.inflate(LayoutInflater.from(context), this, true)

    private var onRangeChangeListener: ((TimeRangePickerAdapter.TimeItem, TimeRangePickerAdapter.TimeItem) -> Unit)? =
        null

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.SimpleDatePicker).apply {
                recycle()
            }
        }
        setDateRecyclerView()
    }

    fun setOnRangeSelectListener(listener: (TimeRangePickerAdapter.TimeItem, TimeRangePickerAdapter.TimeItem) -> Unit) {
        onRangeChangeListener = listener
    }

    private fun setDateRecyclerView() {
        binding.rvTimeRangePicker.apply {
            adapter = TimeRangePickerAdapter(10) { start, end ->
                if (onRangeChangeListener != null) onRangeChangeListener!!(start, end)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}


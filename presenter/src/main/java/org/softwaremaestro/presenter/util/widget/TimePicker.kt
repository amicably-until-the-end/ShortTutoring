package org.softwaremaestro.presenter.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.databinding.WidgetTimePickerBinding
import org.softwaremaestro.presenter.util.widget.adapter.TimeRangePickerAdapter

class TimePicker(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding: WidgetTimePickerBinding =
        WidgetTimePickerBinding.inflate(LayoutInflater.from(context), this, true)

    private var onRangeChangeListener: ((TimeRangePickerAdapter.TimeItem, TimeRangePickerAdapter.TimeItem) -> Unit)? =
        null

    val rvTimePicker: RecyclerView

    init {
        rvTimePicker = binding.rvTimePicker
    }

    fun setOnRangeSelectListener(listener: (TimeRangePickerAdapter.TimeItem, TimeRangePickerAdapter.TimeItem) -> Unit) {
        onRangeChangeListener = listener
    }
}


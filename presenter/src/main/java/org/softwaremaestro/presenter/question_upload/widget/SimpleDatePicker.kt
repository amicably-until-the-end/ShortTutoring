package org.softwaremaestro.presenter.question_upload.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.WidgetSimpleDatePickerBinding

class SimpleDatePicker(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding: WidgetSimpleDatePickerBinding =
        WidgetSimpleDatePickerBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.SimpleDatePicker).apply {
                recycle()
            }
        }
    }

}
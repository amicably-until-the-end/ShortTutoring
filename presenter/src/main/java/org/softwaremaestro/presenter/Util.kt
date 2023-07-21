package org.softwaremaestro.presenter

import android.content.Context
import android.util.TypedValue
import android.widget.Button

object Util {
    fun dpToPx(dp: Int, context: Context): Int {
        val metrics = context.resources.displayMetrics;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }

    fun Button.setEnabledAndChangeColor(enabled: Boolean) {

        isEnabled = enabled
        
        if (enabled) {
            setBackgroundColor(resources.getColor(R.color.primary, null))
            setTextColor(resources.getColor(R.color.white, null))
        } else {
            setBackgroundColor(resources.getColor(R.color.light_grey, null))
            setTextColor(resources.getColor(R.color.grey, null))
        }
    }
}
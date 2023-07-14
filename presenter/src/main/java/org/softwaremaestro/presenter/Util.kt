package org.softwaremaestro.presenter

import android.content.Context
import android.util.TypedValue

object Util {
    fun dpToPx(dp: Int, context: Context): Int {
        val metrics = context.resources.displayMetrics;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }
}
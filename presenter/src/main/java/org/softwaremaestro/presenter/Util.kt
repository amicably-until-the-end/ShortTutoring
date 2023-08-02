package org.softwaremaestro.presenter

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import java.io.ByteArrayOutputStream

object Util {
    fun dpToPx(dp: Int, context: Context): Int {
        val metrics = context.resources.displayMetrics;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }
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

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun requestFocusAndShowKeyboard(view: View, context: Context) {
    view.requestFocus()

    val imm: InputMethodManager =
        context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}
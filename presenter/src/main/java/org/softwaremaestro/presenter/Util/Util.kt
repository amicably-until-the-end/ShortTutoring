package org.softwaremaestro.presenter.Util

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import org.softwaremaestro.presenter.R
import java.io.ByteArrayOutputStream

object Util {
    fun toPx(dp: Int, context: Context): Int {
        val metrics = context.resources.displayMetrics;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }

    fun toDp(px: Int, context: Context): Int {
        val metrics = context.resources.displayMetrics
        return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}

fun View.decreaseWidth(
    l: Int,
    duration: Long,
    context: Context,
    onStart: () -> Unit = {},
    onEnd: () -> Unit = {}
) {

    val anim = ValueAnimator.ofInt(
        measuredWidth,
        measuredWidth - Util.toPx(l, context)
    )

    anim.addUpdateListener { valueAnimator ->
        val layoutParams = layoutParams.apply {
            width = valueAnimator.animatedValue as Int
        }
        this.layoutParams = layoutParams
    }

    anim.duration = duration
    anim.addListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(p0: Animator) {
            onStart()
        }

        override fun onAnimationEnd(p0: Animator) {
            onEnd()
        }

        override fun onAnimationCancel(p0: Animator) {}
        override fun onAnimationRepeat(p0: Animator) {}
    })
    anim.start()
}

fun View.increaseWidth(
    l: Int,
    duration: Long,
    context: Context,
    onStart: () -> Unit = {},
    onEnd: () -> Unit = {}
) {

    val anim = ValueAnimator.ofInt(
        measuredWidth,
        measuredWidth + Util.toPx(l, context)
    )

    anim.addUpdateListener { valueAnimator ->
        val layoutParams = layoutParams.apply {
            width = valueAnimator.animatedValue as Int
        }
        this.layoutParams = layoutParams
    }

    anim.duration = duration
    anim.addListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(p0: Animator) {
            onStart()
        }

        override fun onAnimationEnd(p0: Animator) {
            onEnd()
        }

        override fun onAnimationCancel(p0: Animator) {}
        override fun onAnimationRepeat(p0: Animator) {}
    })
    anim.start()
}

fun Button.setEnabledAndChangeColor(enabled: Boolean) {

    if (enabled) {
        setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
        this.isEnabled = true
        setTextColor(resources.getColor(R.color.white, null))
    } else {
        this.isEnabled = false
        setBackgroundResource(R.drawable.bg_radius_5_grey)
        setTextColor(resources.getColor(R.color.black, null))
    }
}

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val data = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    return "data:image/png;base64,$data"
}

fun requestFocusAndShowKeyboard(view: View, context: Context) {
    view.requestFocus()

    val imm: InputMethodManager =
        context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}
package org.softwaremaestro.presenter.util

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.R
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneId

object Util {
    fun toPx(dp: Int, context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }

    fun toDp(px: Int, context: Context): Int {
        val metrics = context.resources.displayMetrics
        return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun getBottomSheetDialogDefaultHeight(activity: Activity): Int {
        return getWindowHeight(activity) * 60 / 100
        // 기기 높이 대비 비율 설정 부분!!
        // 위 수치는 기기 높이 대비 80%로 다이얼로그 높이를 설정
    }

    private fun getWindowHeight(activity: Activity): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun logError(clazz: Class<*>, message: String) {
        Log.e(clazz.name, message)
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
        setTextColor(resources.getColor(R.color.sub_text_grey, null))
    }
}

fun String.toBitmap(): Bitmap {
    val encodeByteArr = java.util.Base64.getDecoder().decode(this)
    return BitmapFactory.decodeByteArray(encodeByteArr, 0, encodeByteArr.size)
}

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val data = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    return "data:image/png;base64,$data"
}

fun Fragment.moveBack() {
    if (!findNavController().popBackStack()) {
        requireActivity().finish()
    }
}

/**
 * LocalDateTime을 한국어로 변환 ex: 1월 1일 1시 1분
 */
fun LocalDateTime.toKoreanString(): String {
    return "${this.monthValue}월 ${this.dayOfMonth}일 ${this.hour}시 ${
        if (this.minute != 0) "${minute}분" else ""
    }"
}

/**
 * LocalDateTime을 시간만 한국어로 변환 ex: 12:05
 */
fun LocalDateTime.toClockString(): String {
    return "${this.hour}:${String.format("%02d", this.minute)}분"
}

fun showKeyboardAndRequestFocus(view: View) {
    view.requestFocus()
    val imm: InputMethodManager =
        view.context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}

fun hideKeyboardAndRemoveFocus(view: View) {
    val imm =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun nowInKorea() = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

fun RecyclerView.getVerticalSpaceDecoration(
    space: Int,
    context: Context
): RecyclerView.ItemDecoration {
    return VerticalSpaceItemDecoration(space, context)
}

private class VerticalSpaceItemDecoration(
    private val verticalSpaceHeight: Int,
    private val context: Context
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = Util.toPx(verticalSpaceHeight, context)
    }
}


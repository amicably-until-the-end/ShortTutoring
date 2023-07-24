package org.softwaremaestro.presenter.teacher_home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import org.softwaremaestro.presenter.databinding.SnackbarWaitingBinding

class WaitingSnackbar(view: View, text: String, duration: Int) {

    private val context: Context
    private val snackbar: Snackbar
    private val snackbarBinding: SnackbarWaitingBinding

    val isShown: Boolean
        get() = snackbar.isShown

    companion object {
        fun make(view: View, text: String, duration: Int) = WaitingSnackbar(view, text, duration)
    }

    init {

        context = view.context
        snackbar = Snackbar.make(view, text, duration)
        snackbarBinding = LayoutInflater.from(context).let { SnackbarWaitingBinding.inflate(it) }

        (snackbar.view as Snackbar.SnackbarLayout).run {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }

        snackbarBinding.tvNoti.text = text
    }

    fun show() {
        snackbar.show()
    }

    fun dismiss() {
        snackbar.dismiss()
    }

    fun setAction(text: String, action: () -> Unit) {
        snackbarBinding.btnAction.run {
            this.text = text
            setOnClickListener { action() }
        }
    }
}
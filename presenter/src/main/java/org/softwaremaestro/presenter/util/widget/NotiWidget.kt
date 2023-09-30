package org.softwaremaestro.presenter.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.softwaremaestro.presenter.databinding.DialogNotiBinding

class NotiWidget(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding =
        DialogNotiBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.ivCancel.setOnClickListener {
            this@NotiWidget.visibility = GONE
        }

        binding.btnCancel.setOnClickListener {
            this@NotiWidget.visibility = GONE
        }
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setDescription(description: String) {
        binding.tvDesciption.text = description
    }

    fun setOnConfirmClick(onConfirmClick: () -> Unit) {
        binding.btnConfirm.setOnClickListener {
            onConfirmClick()
            this@NotiWidget.visibility = GONE
        }
    }
}

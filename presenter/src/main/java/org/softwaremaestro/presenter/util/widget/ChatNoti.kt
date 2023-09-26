package org.softwaremaestro.presenter.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.softwaremaestro.presenter.databinding.WidgetChatNotiBinding

class ChatNoti(
    context: Context,
    attr: AttributeSet
) : ConstraintLayout(context, attr) {

    private val binding = WidgetChatNotiBinding.inflate(LayoutInflater.from(context), this, true)

    fun setTvNotiMain(text: String) {
        binding.tvNotiMain.text = text
    }

    fun setTvNotiSub(text: String) {
        binding.tvNotiSub.text = text
    }

    fun setBtnPositiveText(text: String) {
        binding.btnPositive.text = text
    }

    fun setBtnNegativeText(text: String) {
        binding.btnNegative.text = text
    }

    fun setOnClickListenerToBtnPositive(onItemClick: OnClickListener) {
        binding.btnPositive.setOnClickListener(onItemClick)
    }

    fun setOnClickListenerToBtnNegative(onItemClick: OnClickListener) {
        binding.btnNegative.setOnClickListener(onItemClick)
    }
}
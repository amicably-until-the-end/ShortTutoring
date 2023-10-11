package org.softwaremaestro.presenter.util.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.softwaremaestro.presenter.databinding.DialogNumberPickerBinding

class NumberPickerBottomDialog(private val onReturnClick: ((Int) -> Unit)) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogNumberPickerBinding

    private var title: String? = null
    private var btnText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNumberPickerBinding.inflate(layoutInflater)
        setDurationPicker()
        setDialogTitle()
        setDialogBtnText()
        setReturnButton()
        setDefaultTime()
        return binding.root
    }

    private fun setDurationPicker() {
        binding.npDuration.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setBtnText(btnText: String) {
        this.btnText = btnText
    }

    private fun setDefaultTime() {
        binding.npDuration.apply {
            minValue = 0
            maxValue = (MAX_VALUE - MIN_VALUE) / STEP
            displayedValues = (MIN_VALUE..MAX_VALUE step 5).map { it.toString() }.toTypedArray()
            value = (DEFAULT - MIN_VALUE) / STEP
        }
    }

    private fun setDialogTitle() {
        if (title != null)
            binding.tvTitle.text = title
    }

    private fun setDialogBtnText() {
        if (btnText != null)
            binding.btnReturn.text = btnText
    }

    private fun setReturnButton() {
        binding.btnReturn.setOnClickListener {
            onReturnClick(binding.npDuration.value)
            dismiss()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        (dialog as BottomSheetDialog).behavior.isDraggable = false

        return dialog
    }

    companion object {
        private const val MIN_VALUE = 10
        private const val MAX_VALUE = 120
        private const val DEFAULT = 20
        private const val STEP = 5
    }
}


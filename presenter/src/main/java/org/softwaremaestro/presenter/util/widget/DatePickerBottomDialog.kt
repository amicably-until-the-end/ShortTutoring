package org.softwaremaestro.presenter.util.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.softwaremaestro.presenter.databinding.DialogDatePickerBinding
import org.softwaremaestro.presenter.util.nowInKorea
import java.time.LocalDate

class DatePickerBottomDialog(private val onReturnClick: ((LocalDate) -> Unit)) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogDatePickerBinding

    private var title: String? = null
    private var btnText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDatePickerBinding.inflate(layoutInflater)

        setDialogTitle()
        setDialogBtnText()
        setReturnButton()
        setDefaultTime()
        return binding.root
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setBtnText(btnText: String) {
        this.btnText = btnText
    }

    private fun setDefaultTime() {
        with(nowInKorea()) {
            binding.dpStart.init(year, monthValue - 1, dayOfMonth, null)
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
            with(binding.dpStart) {
                onReturnClick(LocalDate.of(year, month, dayOfMonth))
            }
            dismiss()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        (dialog as BottomSheetDialog).behavior.isDraggable = false

        return dialog
    }
}


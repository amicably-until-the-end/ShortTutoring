package org.softwaremaestro.presenter.util.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.softwaremaestro.presenter.databinding.DialogTimePickerBinding
import org.softwaremaestro.presenter.util.nowInKorea
import java.time.LocalDateTime

class TimePickerBottomDialog(private val onReturnClick: ((SpecificTime) -> Unit)) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogTimePickerBinding

    private var title: String? = null
    private var btnText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTimePickerBinding.inflate(layoutInflater)
        setTimePicker()
        setDialogTitle()
        setDialogBtnText()
        setReturnButton()
        setDefaultTime()
        return binding.root
    }

    private fun setTimePicker() {
        val now = LocalDateTime.now()
        binding.timePicker.setOnTimeChangedListener { tp, hour, minute ->
            val isTimeValid = now.hour < hour || (now.hour == hour && now.minute <= minute)
            if (!isTimeValid) {
                Toast.makeText(
                    context,
                    "${now.hour}:${now.minute} 이후의 시간을 설정해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.timePicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setBtnText(btnText: String) {
        this.btnText = btnText
    }

    private fun setDefaultTime() {
        with(nowInKorea()) {
            binding.timePicker.hour = hour
            binding.timePicker.minute = minute
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
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            onReturnClick(SpecificTime(hour, minute))
            dismiss()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        (dialog as BottomSheetDialog).behavior.isDraggable = false

        return dialog
    }

    inner class SpecificTime(
        val hour: Int,
        val minute: Int
    ) {
        override fun toString(): String {
            return "${hour}시 ${minute}분"
        }

        override fun equals(other: Any?): Boolean {
            return other is SpecificTime && hour == other.hour && minute == other.minute
        }

        fun toLocalDateTime(): LocalDateTime {
            return LocalDateTime.now().withHour(hour).withMinute(minute)
        }
    }

}


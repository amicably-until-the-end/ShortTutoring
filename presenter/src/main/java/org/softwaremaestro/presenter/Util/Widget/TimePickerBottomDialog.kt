package org.softwaremaestro.presenter.Util.Widget

import android.app.Dialog
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.softwaremaestro.presenter.databinding.DialogTimePickerBinding
import java.text.SimpleDateFormat

class TimePickerBottomDialog(private val onReturnClick: ((SpecificTime) -> Unit)) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogTimePickerBinding

    private var title: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTimePickerBinding.inflate(layoutInflater)

        setDialogTitle()
        setReturnButton()
        setDefaultTime()
        return binding.root
    }

    fun setTitle(title: String) {
        this.title = title
    }

    private fun setDefaultTime() {
        var currentTime = System.currentTimeMillis()
        binding.timePicker.hour = SimpleDateFormat("HH").format(currentTime).toInt()
        binding.timePicker.minute = SimpleDateFormat("mm").format(currentTime).toInt()

    }

    private fun setDialogTitle() {
        if (title != null)
            binding.tvTitle.text = title
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
    }

}


package org.softwaremaestro.presenter.util.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.softwaremaestro.presenter.databinding.DialogDetailAlertBinding

class DetailAlertDialog(
    private val title: String,
    private val description: String,
    private val onConfirmClick: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogDetailAlertBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDetailAlertBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            onConfirmClick()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.tvTitle.text = title
        binding.tvDesciption.text = description
    }
}

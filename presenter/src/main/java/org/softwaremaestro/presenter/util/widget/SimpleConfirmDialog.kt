package org.softwaremaestro.presenter.util.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import org.softwaremaestro.presenter.databinding.DialogDetailAlertBinding

class SimpleConfirmDialog(
    private val onConfirmClick: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogDetailAlertBinding

    var title: String? = null
    var description: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDetailAlertBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        title?.let { binding.tvTitle.text = it }
        description?.let { binding.tvDesciption.text = description }
        binding.btnConfirm.setOnClickListener {
            onConfirmClick()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
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

package org.softwaremaestro.presenter.util.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.softwaremaestro.presenter.databinding.DialogSimpleAlertBinding

class SimpleAlertDialog : DialogFragment() {

    private lateinit var binding: DialogSimpleAlertBinding

    var title: String? = null
    var description: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSimpleAlertBinding.inflate(layoutInflater)
        binding.tvTitle.text = title
        if (description.isNullOrEmpty()) {
            binding.tvDesciption.visibility = View.GONE
        } else {
            binding.tvDesciption.text = description
        }
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }
}

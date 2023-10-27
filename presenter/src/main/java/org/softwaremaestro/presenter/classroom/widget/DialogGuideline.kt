package org.softwaremaestro.presenter.classroom.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.softwaremaestro.presenter.databinding.DialogGuidelineBinding

class DialogGuideline : DialogFragment() {

    private lateinit var binding: DialogGuidelineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogGuidelineBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.containerConfirm.setOnClickListener {
            dismiss()
        }
    }
}
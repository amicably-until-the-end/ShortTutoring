package org.softwaremaestro.presenter.Util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.softwaremaestro.presenter.databinding.DialogDetailAlertBinding

class DetailAlertDialog(private val onConfirmClick: () -> Unit) : DialogFragment() {

    private lateinit var binding: DialogDetailAlertBinding

    private var title: String? = null
    private var description: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDetailAlertBinding.inflate(layoutInflater)

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnConfirm.setOnClickListener {
            //on click()
            onConfirmClick()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        if (!title.isNullOrEmpty()) binding.tvTitle.text = title
        if (!description.isNullOrEmpty()) binding.tvDesciption.text = description

        return binding.root
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setDescription(desc: String) {
        this.description = desc
    }

}

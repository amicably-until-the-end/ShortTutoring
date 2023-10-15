package org.softwaremaestro.presenter.question_upload.question_normal_upload.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.softwaremaestro.presenter.databinding.DialogSchoolLevelBinding

class DialogSchoolLevel(private val onSchoolClick: ((String) -> Unit)) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogSchoolLevelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSchoolLevelBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSchoolMiddle.setOnClickListener {
            onSchoolClick("중학교")
            dismiss()
        }
        binding.btnSchoolHigh.setOnClickListener {
            onSchoolClick("고등학교")
            dismiss()
        }
        binding.btnDontKnow.setOnClickListener {
            onSchoolClick("모르겠어요")
            dismiss()
        }
    }
}
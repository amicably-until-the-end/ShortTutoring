package org.softwaremaestro.presenter.question_upload.question_normal_upload.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.softwaremaestro.presenter.databinding.DialogSchoolSubjectBinding

class DialogSchoolSubject(
    private val onSubjectClick: ((String) -> Unit)
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogSchoolSubjectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSchoolSubjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.npSubject.apply {
            minValue = 0
            maxValue = 6
            displayedValues = subjects
            value = 0
        }

        binding.btnSelect.setOnClickListener {
            onSubjectClick(subjects[binding.npSubject.value])
            dismiss()
        }
    }

    companion object {
        private val subjects = arrayOf(
            "고등수학(상)", "고등수학(하)", "수학1", "수학2", "미적분", "확률과 통계", "기하"
        )
    }
}
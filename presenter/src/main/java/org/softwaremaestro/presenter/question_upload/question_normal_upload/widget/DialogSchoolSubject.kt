package org.softwaremaestro.presenter.question_upload.question_normal_upload.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.softwaremaestro.presenter.databinding.DialogSchoolSubjectBinding

class DialogSchoolSubject(
    private val schoolLevel: String,
    private val onSubjectClick: ((String) -> Unit)
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogSchoolSubjectBinding
    private lateinit var subjects: Array<String>

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

        setSubjects()
        setSubjectNumberPicker()
        setBtnSelect()
    }

    private fun setSubjects() {
//        subjects = when (schoolLevel) {
//            "중학교" -> subjects_middle
//            else -> subjects_high
//        }
        subjects = subjects_school
    }

    private fun setSubjectNumberPicker() {
        binding.npSubject.apply {
            minValue = 0
            maxValue = subjects.size - 1
            displayedValues = subjects
            value = 0
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }
    }

    private fun setBtnSelect() {
        binding.btnSelect.setOnClickListener {
            onSubjectClick(subjects[binding.npSubject.value])
            dismiss()
        }
    }

    companion object {
        private val subjects_middle = arrayOf(
            "1학년 1학기", "1학년 2학기", "2학년 1학기", "2학년 2학기", "3학년 1학기", "3학년 2학기", "모르겠어요"
        )
        private val subjects_high = arrayOf(
            "고등수학(상)", "고등수학(하)", "수학1", "수학2", "미적분", "확률과 통계", "기하", "모르겠어요"
        )
        private val subjects_school = arrayOf(
            "수학", "과학", "국어", "영어", "기타"
        )
    }
}
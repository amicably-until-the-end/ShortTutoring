package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormSchoolLevelBinding
import org.softwaremaestro.presenter.question_upload.viewmodel.QuestionUploadViewModel

class QuestionFormSchoolLevelFragment : Fragment() {

    private lateinit var binding: FragmentQuestionFormSchoolLevelBinding
    private val viewModel: QuestionUploadViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionFormSchoolLevelBinding.inflate(layoutInflater)

        setToolBar()

        setRadioGroup()

        return binding.root
    }


    private fun setRadioGroup() {

        binding.rgSchoolLevel.setOnCheckedChangeListener { parent, resId ->

            val selectedSchoolLevel =
                when (resId) {
                    R.id.rb_middle_school -> "중학교"
                    R.id.rb_high_school -> "고등학교"
                    else -> null
                }

            selectedSchoolLevel?.let {
                if (viewModel.school.value == it) {
                    binding.rgSchoolLevel.clearCheck()
                    viewModel._school.postValue(null)
                    return@setOnCheckedChangeListener
                }
                viewModel._school.postValue(it)

                if (viewModel.subject.value == null) {
                    findNavController().navigate(
                        R.id.action_questionFormSchoolLevelFragment_to_questionFormSubjectFragment
                    )
                } else {
                    findNavController().popBackStack(R.id.questionFormFragment, false)
                }
            }
        }

    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}
package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormSubjectBinding

class QuestionFormSubjectFragment : Fragment() {

    private lateinit var binding: FragmentQuestionFormSubjectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionFormSubjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rgSubject.setOnCheckedChangeListener { parent, resId ->

            val selectedSubject =
                when (resId) {
                    R.id.rb_high_school_math_1 -> "고등 수학(상)"
                    R.id.rb_high_school_math_2 -> "고등 수학(하)"
                    R.id.rb_math_1 -> "수학1"
                    R.id.rb_math_2 -> "수학2"
                    R.id.rb_ps -> "확률과 통계"
                    else -> null
                }

            selectedSubject?.let {

                (requireActivity() as QuestionUploadActivity).subjectSelected = it

                findNavController().navigate(
                    R.id.action_questionFormSubjectFragment_to_questionFormDifficultyFragment
                )
            }
        }
    }
}
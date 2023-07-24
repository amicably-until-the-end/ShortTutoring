package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormDifficultyBinding

const val INPUT_DIFFICULTY = "input-difficulty"

class QuestionFormDifficultyFragment : Fragment() {

    private lateinit var binding: FragmentQuestionFormDifficultyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionFormDifficultyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rgDifficulty.setOnCheckedChangeListener { _, resId ->

            val selectedDifficulty =
                when (resId) {
                    R.id.rb_easy -> "쉬움"
                    R.id.rb_middle -> "중간"
                    R.id.rb_difficult -> "어려움"
                    else -> null
                }

            selectedDifficulty?.let {
                findNavController().navigate(
                    R.id.action_questionFormDifficultyFragment_to_questionFormFragment,
                    bundleOf(
                        INPUT_DESCRIPTION to arguments?.getString(INPUT_DESCRIPTION),
                        INPUT_SCHOOL_LEVEL to arguments?.getString(INPUT_SCHOOL_LEVEL),
                        INPUT_SUBJECT to arguments?.getString(INPUT_SUBJECT),
                        INPUT_DIFFICULTY to selectedDifficulty
                    )
                )
            }
        }
    }
}
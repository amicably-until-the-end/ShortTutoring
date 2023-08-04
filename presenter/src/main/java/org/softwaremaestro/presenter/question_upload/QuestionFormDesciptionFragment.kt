package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormDescriptionBinding
import org.softwaremaestro.presenter.question_upload.viewmodel.QuestionUploadViewModel
import org.softwaremaestro.presenter.requestFocusAndShowKeyboard

private const val IME_ACTION = EditorInfo.IME_ACTION_NEXT

class QuestionFormDescriptionFragment : Fragment() {

    private lateinit var binding: FragmentQuestionFormDescriptionBinding

    private val viewModel: QuestionUploadViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionFormDescriptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 자동으로 질문 내용 텍스트필드에 포커스가 가게 한다
        requestFocusAndShowKeyboard(binding.atvContent, requireContext())

        // 다음 버튼을 누르면 질문 입력 페이지로 돌아간다
        setOnEditorActionLister()
    }

    private fun setOnEditorActionLister() {
        binding.atvContent.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == IME_ACTION) {
                view.text.toString().let {
                    viewModel._description.postValue(it)
                }
                if (viewModel.school.value == null) {
                    findNavController().navigate(
                        R.id.action_questionFormDescriptionFragment_to_questionFormSchoolLevelFragment,
                    )
                } else {
                    findNavController().popBackStack(R.id.questionFormFragment, false)
                }

            }
            true
        }
    }
}
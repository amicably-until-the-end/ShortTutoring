package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.FragmentSearchYearOfAdmissionBinding
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.requestFocusAndShowKeyboard

const val SELECTED_YEAR_OF_ADMISSION = "selected-year-of-admission"
private const val IME_ACTION = IME_ACTION_NEXT

@AndroidEntryPoint
class SearchYearOfAdmissionFragment : Fragment() {
    private lateinit var binding: FragmentSearchYearOfAdmissionBinding

    private val viewModel: TeacherRegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchYearOfAdmissionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 자동으로 학번 텍스트필드에 포커스가 가게 한다
        requestFocusAndShowKeyboard(binding.atvYearOfAdmission, requireContext())

        // 다음 버튼을 누르면 선생님 정보 입력 페이지로 돌아간다
        setOnEditorActionLister(binding.atvYearOfAdmission)
    }

    private fun setOnEditorActionLister(textView: TextView) {
        textView.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == IME_ACTION) {
                viewModel._admissonYear.value = binding.tfYearOfAdmission.editText?.text.toString()
                findNavController().popBackStack()
            }
            true
        }
    }
}
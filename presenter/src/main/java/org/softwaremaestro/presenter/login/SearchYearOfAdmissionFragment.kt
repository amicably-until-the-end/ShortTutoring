package org.softwaremaestro.presenter.login

import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentSearchYearOfAdmissionBinding

const val SELECTED_YEAR_OF_ADMISSION = "selected-year-of-admission"
private const val IME_ACTION = IME_ACTION_NEXT

class SearchYearOfAdmissionFragment : Fragment() {
    private lateinit var binding: FragmentSearchYearOfAdmissionBinding

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
        requestFocusAndShowKeyboard(binding.atvYearOfAdmission)

        // 다음 버튼을 누르면 선생님 정보 입력 페이지로 돌아간다
        setOnEditorActionLister(binding.atvYearOfAdmission)
    }

    private fun setOnEditorActionLister(textView: TextView) {
        textView.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == IME_ACTION) {
                findNavController().navigate(
                    R.id.action_searchYearOfAdmissionFragment_to_registerTeacherInfoFragment,
                    bundleOf(
                        SELECTED_YEAR_OF_ADMISSION to view.text.toString(),
                        SELECTED_MAJOR to arguments?.getString(SELECTED_MAJOR),
                        SELECTED_COLLEGE to arguments?.getString(SELECTED_COLLEGE),
                        SELECTED_UNIV to arguments?.getString(SELECTED_UNIV)
                    )
                )
            }
            true
        }
    }

    private fun requestFocusAndShowKeyboard(view: View) {
        view.requestFocus()

        val imm: InputMethodManager =
            requireContext().getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }
}
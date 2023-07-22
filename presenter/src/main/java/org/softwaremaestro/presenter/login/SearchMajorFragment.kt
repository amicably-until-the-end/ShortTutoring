package org.softwaremaestro.presenter.login

import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentSearchMajorBinding

const val SELECTED_MAJOR = "selected-major"
private const val IME_ACTION = IME_ACTION_NEXT

class SearchMajorFragment : Fragment() {
    private lateinit var binding: FragmentSearchMajorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchMajorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전공 텍스트뷰에 자동완성 기능을 담당하는 어댑터를 연결한다
        setAdapter(binding.atvMajor)

        // 자동으로 전공 텍스트필드에 포커스가 가게 한다
        requestFocusAndShowKeyboard(binding.atvMajor)
    }

    private fun setAdapter(view: AutoCompleteTextView) {

        val adapter =
            SearchAdapter(
                requireActivity(),
                listOf("수학교육과", "영어교육과", "국어교육과")
            ) { name: String ->
                findNavController().navigate(
                    R.id.action_searchMajorFragment_to_searchYearOfAdmissionFragment,
                    bundleOf(
                        SELECTED_MAJOR to name,
                        SELECTED_COLLEGE to arguments?.getString(SELECTED_COLLEGE),
                        SELECTED_UNIV to arguments?.getString(SELECTED_UNIV)
                    )
                )
            }

        view.setAdapter(adapter)
    }

    private fun requestFocusAndShowKeyboard(view: View) {
        view.requestFocus()

        val imm: InputMethodManager =
            requireContext().getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }
}
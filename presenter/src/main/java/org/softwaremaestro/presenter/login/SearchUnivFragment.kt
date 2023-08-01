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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentSeachUnivBinding
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel

const val SELECTED_UNIV = "selected-univ"
private const val IME_ACTION = IME_ACTION_NEXT

@AndroidEntryPoint
class SearchUnivFragment : Fragment() {

    private lateinit var binding: FragmentSeachUnivBinding

    private val viewModel: TeacherRegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSeachUnivBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 대학 텍스트뷰에 자동완성 기능을 담당하는 어댑터를 연결한다
        setAdapter(binding.atvUniv)

        // 자동으로 대학 텍스트필드에 포커스가 가게 한다
        requestFocusAndShowKeyboard(binding.atvUniv)
    }

    private fun setAdapter(view: AutoCompleteTextView) {

        val adapter =
            SearchAdapter(
                requireActivity(),
                listOf("서울대학교", "연세대학교", "고려대학교", "성균관대학교")
            ) { name: String ->
                viewModel._univ.value = name
                findNavController().navigate(
                    R.id.action_searchUnivFragment_to_searchCollegeFragment,
                    bundleOf(SELECTED_UNIV to name)
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
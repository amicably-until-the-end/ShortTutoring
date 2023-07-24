package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentSearchCollegeBinding
import org.softwaremaestro.presenter.requestFocusAndShowKeyboard

const val SELECTED_COLLEGE = "selected-college"
private const val IME_ACTION = EditorInfo.IME_ACTION_NEXT

class SearchCollegeFragment : Fragment() {
    private lateinit var binding: FragmentSearchCollegeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchCollegeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 대학 텍스트뷰에 자동완성 기능을 담당하는 어댑터를 연결한다
        setAdapter(binding.atvCollege)

        // 자동으로 대학 텍스트필드에 포커스가 가게 한다
        requestFocusAndShowKeyboard(binding.atvCollege, requireContext())
    }

    private fun setAdapter(view: AutoCompleteTextView) {

        val adapter =
            SearchAdapter(
                requireActivity(),
                listOf("의과대학", "경영대학", "사범대학")
            ) { name: String ->
                findNavController().navigate(
                    R.id.action_searchCollegeFragment_to_searchMajorFragment,
                    bundleOf(
                        SELECTED_COLLEGE to name,
                        SELECTED_UNIV to arguments?.getString(SELECTED_UNIV)
                    )
                )
            }


        view.setAdapter(adapter)
    }
}
package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentSearchCollegeBinding
import org.softwaremaestro.presenter.login.adapter.SearchAdapter
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.requestFocusAndShowKeyboard

const val SELECTED_COLLEGE = "selected-college"
private const val IME_ACTION = EditorInfo.IME_ACTION_NEXT

@AndroidEntryPoint
class SearchCollegeFragment : Fragment() {
    private lateinit var binding: FragmentSearchCollegeBinding

    private val viewModel: TeacherRegisterViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchCollegeBinding.inflate(layoutInflater)
        setToolBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 대학 텍스트뷰에 자동완성 기능을 담당하는 어댑터를 연결한다
        setAdapter(binding.atvCollege)


        Log.d("mymymy", "share viewmodel ${viewModel.univ}")

        // 자동으로 대학 텍스트필드에 포커스가 가게 한다
        requestFocusAndShowKeyboard(binding.atvCollege, requireContext())
    }

    private fun setAdapter(view: AutoCompleteTextView) {

        val adapter =
            SearchAdapter(
                requireActivity(),
                listOf("의과대학", "경영대학", "사범대학")
            ) { name: String ->
                viewModel._college.value = name
                findNavController().navigate(R.id.action_searchCollegeFragment_to_searchMajorFragment)
            }


        view.setAdapter(adapter)
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
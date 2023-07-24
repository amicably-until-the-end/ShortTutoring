package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterTeacherInfoBinding
import org.softwaremaestro.presenter.setEnabledAndChangeColor

// 회원가입 두 번째 화면.
// 개인정보를 입력한다.
@AndroidEntryPoint
class RegisterTeacherInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterTeacherInfoBinding

    private val ets: List<TextInputEditText> by lazy {
        with(binding) {
            listOf(etUniv, etCollege, etMajor, etYearOfAdmission)
        }
    }

    private val actions = listOf(
        R.id.action_registerTeacherInfoFragment_to_searchUnivFragment,
        R.id.action_registerTeacherInfoFragment_to_searchCollegeFragment,
        R.id.action_registerTeacherInfoFragment_to_searchMajorFragment,
        R.id.action_registerTeacherInfoFragment_to_searchYearOfAdmissionFragment
    )

    private val selectedValues = listOf(
        SELECTED_UNIV,
        SELECTED_COLLEGE,
        SELECTED_MAJOR,
        SELECTED_YEAR_OF_ADMISSION
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterTeacherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰를 클릭하면 값을 입력하는 페이지로 이동한다
        setOnClickListener()

        // 선생님이 입력한 값을 뷰에 표시한다
        setSelectedValues()

        // 다음 버튼을 누르면 로고화면으로 돌아간다
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_registerTeacherInfoFragment_to_logoFragment)
        }

        // 입력받지 않은 에딧텍스트가 있으면 버튼을 활성화하지 않는다
        isAllValuesEntered().let {
            binding.btnNext.setEnabledAndChangeColor(it)
        }
    }

    private fun setOnClickListener() {
        ets.zip(actions).forEach { pair ->
            pair.first.setOnClickListener {
                findNavController().navigate(pair.second)
            }
        }
    }

    private fun setSelectedValues() {
        selectedValues.zip(ets).forEach { pair ->
            arguments?.getString(pair.first)?.let {
                pair.second.setText(it)
            }
        }
    }

    private fun isAllValuesEntered() = ets.map { it.text.isNullOrEmpty() }.contains(true).toggle()

    private fun Boolean.toggle() = !this
}
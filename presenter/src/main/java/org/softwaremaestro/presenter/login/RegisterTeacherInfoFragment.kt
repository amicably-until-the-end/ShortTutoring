package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterTeacherInfoBinding
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.hideKeyboardAndRemoveFocus
import org.softwaremaestro.presenter.util.Util.toPx
import java.lang.Integer.min

// 회원가입 두 번째 화면.
// 개인정보를 입력한다.
@AndroidEntryPoint
class RegisterTeacherInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterTeacherInfoBinding
    private val viewModel: TeacherRegisterViewModel by activityViewModels()
    private var registerEnabled = false
    private var isSmallSizeScreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterTeacherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportSmallScreenSize()
        loadViewModelValue()
        setAtvUniv()
        setEtTeacherMajor()
        setNextButton()
        setToolBar()
        observe()
    }

    private fun supportSmallScreenSize() {
        val width = Util.getWidth(requireActivity())
        isSmallSizeScreen = width < 600
        if (isSmallSizeScreen) {
            val paddingValue = toPx(30, requireContext())
            binding.glLeft.setGuidelineBegin(paddingValue)
            binding.glRight.setGuidelineEnd(paddingValue)
        }
    }

    private fun loadViewModelValue() {
        binding.atvUniv.setText(viewModel.schoolName.value)
        binding.etTeacherMajor.setText(viewModel.major.value)
    }

    private fun setEtTeacherMajor() {
        binding.etTeacherMajor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel._major.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setAtvUniv() {
        binding.atvUniv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0.toString().let { schoolName ->
                    viewModel._schoolName.value = schoolName
                    viewModel.suggestSchoolNames(schoolName)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setNextButton() {
        binding.btnNext.setOnClickListener {
            if (registerEnabled) {
//                findNavController().navigate(R.id.action_registerTeacherInfoFragment_to_univAuthFragment)
                findNavController().navigate(R.id.action_registerTeacherInfoFragment_to_completeTeacherProfileFragment)
            } else {
                Util.createToast(requireActivity(), "출신 대학과 학과를 입력해주세요").show()
            }
        }
    }


    private fun observe() {
        observeSchoolNameSuggestions()
        observeSchoolNameAndMajorProper()
    }

    private fun observeSchoolNameSuggestions() {
        viewModel.schoolNameSuggestions.observe(viewLifecycleOwner) { univs ->

            // 드랍다운이 계속 뜨는 버그 방지
            if (univs.find { it == binding.atvUniv.text.toString() } != null) return@observe

            val univAdapter = ArrayAdapter(requireContext(), R.layout.item_univ, univs)
            binding.atvUniv.setAdapter(univAdapter)

            val itemHeight = toPx(50, requireContext())
            binding.atvUniv.dropDownHeight = min(MAX_ITEM_CNT, univs.size) * itemHeight
            binding.atvUniv.showDropDown()

            binding.atvUniv.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, _, _ ->
                    hideKeyboardAndRemoveFocus(binding.etTeacherMajor)
                }
        }
    }

    private fun observeSchoolNameAndMajorProper() {
        viewModel.schoolNameAndMajorProper.observe(viewLifecycleOwner) { proper ->
            with(binding.btnNext) {
                if (proper) {
                    setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                    setTextColor(resources.getColor(R.color.white, null))
                } else {
                    setBackgroundResource(R.drawable.bg_radius_5_grey)
                    setTextColor(resources.getColor(R.color.sub_text_grey, null))
                }
            }
            registerEnabled = proper
        }
    }

    companion object {
        private const val MAX_ITEM_CNT = 6
    }
}
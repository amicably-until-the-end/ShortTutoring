package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterStudentInfoBinding
import org.softwaremaestro.presenter.login.viewmodel.StudentRegisterViewModel
import org.softwaremaestro.presenter.util.Util

@AndroidEntryPoint
class RegisterStudentInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterStudentInfoBinding
    private val viewModel: StudentRegisterViewModel by activityViewModels()
    private var registerEnabled = false
    private var isSmallSizeScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterStudentInfoBinding.inflate(layoutInflater)
        supportSmallScreenSize()
        setRgSchoolLevel()
        setRgSchoolGrade()
        setBtnNext()
        setBtnToolbarBack()
        observe()

        return binding.root
    }

    private fun supportSmallScreenSize() {
        val width = Util.getWidth(requireActivity())
        isSmallSizeScreen = width < 600
        if (isSmallSizeScreen) {
            val paddingValue = Util.toDp(20, requireContext())
            binding.tvSchoolLevel.setPadding(paddingValue, 0, paddingValue, 0)
            binding.rgSchoolLevel.setPadding(paddingValue, 0, paddingValue, 0)
            binding.tvGrade.setPadding(paddingValue, 0, paddingValue, 0)
            binding.rgGrade.setPadding(paddingValue, 0, paddingValue, 0)
        }
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setRgSchoolLevel() {
        binding.rgSchoolLevel.setOnCheckedChangeListener { _, resId ->
            when (resId) {
                R.id.rb_middle_school -> "중학교"
                R.id.rb_high_school -> "고등학교"
                else -> null
            }?.let {
                viewModel.setSchoolLevel(it)
            }
        }
    }

    private fun setRgSchoolGrade() {
        binding.rgGrade.setOnCheckedChangeListener { _, resId ->
            when (resId) {
                R.id.rb_grade_0 -> 1
                R.id.rb_grade_1 -> 2
                R.id.rb_grade_2 -> 3
                else -> null
            }?.let {
                viewModel.setSchoolGrade(it)
            }
        }
    }

    private fun setBtnNext() {
        binding.btnNext.setOnClickListener {
            if (registerEnabled) {
                findNavController().navigate(R.id.action_registerStudentInfoFragment_to_completeStudentProfileFragment)
            } else {
                Util.createToast(requireActivity(), "학교와 학년을 선택해주세요").show()
            }
        }
    }

    private fun setBtnToolbarBack() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observe() {
        observeSchoolLevel()
        observeSchoolLevelAndGrade()
    }

    private fun observeSchoolLevel() {
        viewModel.schoolLevel.observe(viewLifecycleOwner) {
            binding.containerSchoolGrade.visibility = View.VISIBLE
        }
    }

    private fun observeSchoolLevelAndGrade() {
        viewModel.schoolLevelAndGradeProper.observe(viewLifecycleOwner) { proper ->
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
}